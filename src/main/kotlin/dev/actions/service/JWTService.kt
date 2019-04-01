package dev.actions.service

import dev.actions.dto.UserDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*


/**
 * @author vladov 2019-03-31
 */
@Component
class JWTService : Serializable {

    @Value("\${jwtauth.jjwt.secret}")
    private lateinit var secret: String

    @Value("\${jwtauth.jjwt.expiration}")
    private lateinit var expirationTime: String

    fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(Base64.getEncoder()
                .encodeToString(secret.toByteArray())).parseClaimsJws(token).body
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
        val expirationTimeLong = java.lang.Long.parseLong(expirationTime)
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + expirationTimeLong * 1000)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .compact()
    }

    fun validateToken(token: String): Boolean {
        return (!isTokenExpired(token))
    }
}
