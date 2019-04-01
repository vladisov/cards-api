package dev.actions.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.actions.config.PBKDF2Encoder
import dev.actions.domain.Item
import dev.actions.service.JWTService
import dev.actions.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication

@ExtendWith(SpringExtension::class)
@WebMvcTest(AuthenticationController::class)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
//@ContextConfiguration(classes = [WebSecurityConfig::class])
@Disabled
//TODO
class AuthenticationControllerTest {

    @MockBean
    private lateinit var jwtService: JWTService
    @MockBean
    private lateinit var passwordEncoder: PBKDF2Encoder
    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var webClient: WebTestClient


    @BeforeEach
    fun setup() {
        webClient = WebTestClient
                .bindToController(AuthenticationController())
                .apply<WebTestClient.ControllerSpec>(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .build()
    }

    @Test
    fun testGetItemsForbidden() {
        val result: FluxExchangeResult<Item> = webClient.get().uri("/api/item")
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(Item::class.java)

    }
}
