package dev.actions.controller

import dev.actions.config.PBKDF2Encoder
import dev.actions.domain.Role
import dev.actions.domain.User
import dev.actions.dto.AuthRequest
import dev.actions.dto.AuthResponse
import dev.actions.dto.UserDto
import dev.actions.service.JWTService
import dev.actions.service.UserService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
@WebMvcTest(AuthenticationController::class)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @MockBean
    private lateinit var jwtService: JWTService
    @MockBean
    private lateinit var passwordEncoder: PBKDF2Encoder
    @MockBean
    private lateinit var userService: UserService

    private lateinit var webClient: WebTestClient
    private lateinit var authRequest: AuthRequest
    private lateinit var userDto: UserDto
    private lateinit var user: User


    @BeforeEach
    fun setup() {
        webClient = WebTestClient
                .bindToController(AuthenticationController(jwtService, passwordEncoder, userService))
                .apply<WebTestClient.ControllerSpec>(springSecurity())
                .build()

        authRequest = AuthRequest("dummy", "user")
        userDto = UserDto("dummy", "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", true, listOf(Role.ROLE_USER))
        user = User("id", "dummy", "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", true, listOf(Role.ROLE_USER))
    }

    @Test
    fun testAuthSuccess() {
        given(userService.findByUsername(authRequest.username)).willReturn(Mono.just(userDto))
        given(passwordEncoder.matches(authRequest.password, userDto.password)).willReturn(true)
        given(jwtService.generateToken(userDto)).willReturn("token")

        val result: FluxExchangeResult<AuthResponse> =
                webClient.post()
                        .uri("/auth")
                        .syncBody(authRequest)
                        .exchange()
                        .expectStatus().isOk
                        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                        .returnResult(AuthResponse::class.java)

        val authResponse: Flux<AuthResponse> = result.responseBody
        StepVerifier
                .create(authResponse)
                .expectSubscription()
                .assertNext { response ->
                    Assertions.assertThat(response).isNotNull
                    Assertions.assertThat(response.token).isEqualTo("token")
                }
                .expectComplete()
                .verify()

    }

    @Test
    fun testAuthUserNotFoundUnauthorized() {
        given(userService.findByUsername(authRequest.username)).willReturn(Mono.empty())

        webClient.post()
                .uri("/auth")
                .syncBody(authRequest)
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun testRegisterSuccess() {
        given(userService.findByUsername(authRequest.username)).willReturn(Mono.empty())
        given(userService.save(authRequest.username, authRequest.password)).willReturn(Mono.just(user))
        given(passwordEncoder.encode(authRequest.password)).willReturn(authRequest.password)

        webClient.post()
                .uri("/register")
                .syncBody(authRequest)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(AuthResponse::class.java)
    }

    @Test
    fun testRegisterUserExistsBadRequest() {
        given(userService.findByUsername(authRequest.username)).willReturn(Mono.just(userDto))

        webClient.post()
                .uri("/register")
                .syncBody(authRequest)
                .exchange()
                .expectStatus().isBadRequest
    }

    @Test
    fun testSaveServerError() {
        given(userService.findByUsername(authRequest.username)).willReturn(Mono.empty())
        given(userService.save(authRequest.username, authRequest.password)).willReturn(Mono.empty())

        webClient.post()
                .uri("/register")
                .syncBody(authRequest)
                .exchange()
                .expectStatus().is5xxServerError
    }
}
