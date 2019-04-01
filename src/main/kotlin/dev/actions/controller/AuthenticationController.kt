package dev.actions.controller

import dev.actions.config.PBKDF2Encoder
import dev.actions.dto.AuthRequest
import dev.actions.dto.AuthResponse
import dev.actions.dto.UserDto
import dev.actions.service.JWTService
import dev.actions.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


/**
 * @author vladov 2019-03-31
 */
@RestController
class AuthenticationController {

    @Autowired
    private lateinit var jwtService: JWTService

    @Autowired
    private lateinit var passwordEncoder: PBKDF2Encoder

    @Autowired
    private lateinit var userService: UserService

    @PostMapping(value = ["/auth"])
    fun auth(@RequestBody ar: AuthRequest): Mono<ResponseEntity<AuthResponse>> {
        val users: Mono<UserDto> = userService.findByUsername(ar.username)
        return users.map { userDetails ->
            if (passwordEncoder.matches(ar.password, userDetails.password)) {
                ResponseEntity.ok(AuthResponse(jwtService.generateToken(userDetails)))
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        }.defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
    }

    @PostMapping(value = ["/register"])
    fun register(@RequestBody ar: AuthRequest): Mono<ResponseEntity<String>> {
        val users: Mono<UserDto> = userService.findByUsername(ar.username)
        val map = users.map {
            ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("user already exists")
        }
        return createUser(ar)
    }

    fun createUser(ar: AuthRequest): Mono<ResponseEntity<String>> {
        return userService.save(ar.username, passwordEncoder.encode(ar.password))
                .flatMap { user ->
                    ResponseEntity.ok(user.username)
                }
    }
}
