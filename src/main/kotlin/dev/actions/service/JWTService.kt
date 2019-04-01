package dev.actions.service

import dev.actions.config.KeyProps
import dev.actions.dto.UserDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*


/**
 * @author vladov 2019-03-31
 */
@Component
class JWTService : Serializable {

    @Autowired
    private lateinit var keyProps: KeyProps

    fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(Base64.getEncoder()
                .encodeToString(keyProps.secret.toByteArray())).parseClaimsJws(token).body
    }

    fun getUsernameFromToken(token: String): String {
        return getAllClaimsFromToken(token).subject
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getAllClaimsFromToken(token).expiration
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(userDto: UserDto): String {
        val claims = HashMap<String, Any>()
        claims["role"] = userDto.roles
        return doGenerateToken(claims, userDto.username)
    }

    private fun doGenerateToken(claims: Map<String, Any>, username: String): String {
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + keyProps.expiration * 1000)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(keyProps.secret.toByteArray()))
                .compact()
    }

    fun validateToken(token: String): Boolean {
        return (!isTokenExpired(token))
    }
}
