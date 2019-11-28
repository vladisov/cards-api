package dev.cards.config

import dev.cards.domain.Role
import dev.cards.service.JWTService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*
import java.util.stream.Collectors


/**
 * @author vladov 2019-03-31
 */
@Component
class AuthenticationManager : ReactiveAuthenticationManager {

    @Autowired
    private lateinit var jwtService: JWTService

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()

        var userId: String?
        try {
            userId = jwtService.getSubjectFromToken(authToken)
        } catch (e: Exception) {
            userId = null
        }

        if (userId != null && jwtService.validateToken(authToken)) {
            val claims = jwtService.getAllClaimsFromToken(authToken)
            val rolesMap: List<*> = claims.get("role", List::class.java)
            val roles = ArrayList<Role>()

            for (rolemap in rolesMap) {
                roles.add(Role.valueOf(rolemap as String))
            }

            val auth = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    roles.stream()
                            .map { authority -> SimpleGrantedAuthority(authority.name) }
                            .collect(Collectors.toList())
            )

            return Mono.just(auth)
        } else {
            return Mono.empty()
        }
    }
}
