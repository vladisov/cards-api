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
    fun auth(@RequestBody request: AuthRequest): Mono<ResponseEntity<AuthResponse>> {
        val users: Mono<UserDto> = userService.findByUsername(request.username)
        return users.map { userDetails ->
            when {
                passwordEncoder.matches(request.password, userDetails.password) ->
                    ResponseEntity.ok(AuthResponse(jwtService.generateToken(userDetails)))
                else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        }.defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
    }

    @PostMapping(value = ["/register"])
    //TODO exception handling
    fun register(@RequestBody request: AuthRequest): Mono<ResponseEntity<String>> {
        val users: Mono<UserDto> = userService.findByUsername(request.username)
        return users.map { ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with the username already exists!") }
                .switchIfEmpty(createUser(request))
    }

    private fun createUser(ar: AuthRequest): Mono<ResponseEntity<String>> {
        return Mono.defer {
            val saved = userService.save(ar.username, passwordEncoder.encode(ar.password))
            saved.map { ResponseEntity.ok().body("User saved!") }
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Smth went wrong, user wasn't created."))
        }
    }
}
