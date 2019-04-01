package dev.actions.service

import dev.actions.AbstractTest
import dev.actions.config.KeyProps
import dev.actions.domain.Role
import dev.actions.dto.UserDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class JWTServiceTest : AbstractTest() {

    private lateinit var keyProps: KeyProps
    private lateinit var jwtService: JWTService
    private val token = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiJ1c2VyMTEiLCJpYXQiOjE1NTQxMTIwODksImV4cCI6MTU1NDE0MDk2OX0.UBJ3bj966Xtmm57FzH4qJdbF1QIzCMYy-YE-9b1m_ZIElF8SnC4Wc59T0pKeXVRA0etkGcEcg5pyhHKDhEWI0Q"

    @BeforeEach
    fun setup() {
        keyProps = KeyProps()
        keyProps.secret = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave512bitsKeySize"
        keyProps.expiration = 100000
        jwtService = JWTService(keyProps)
    }

    @Test
    fun testGetExpirationDateFromTokenSuccess() {
        val dateFromToken = jwtService.getExpirationDateFromToken(token)
        assertThat(dateFromToken).isNotNull()
        assertThat(dateFromToken).isEqualTo(Date(1554140969000))
    }

    @Test
    fun generateTokenSuccess() {
        val generatedToken = jwtService.generateToken(UserDto("user", "user", true, listOf(Role.ROLE_USER)))
        assertThat(generatedToken).isNotNull()
    }

    @Test
    fun testValidateTokenIsNotValid() {
        val isTokenValid = jwtService.validateToken(token)
        assertThat(isTokenValid).isNotNull()
        assertThat(isTokenValid).isFalse()
    }

    @Test
    fun testGetAllClaimsFromTokenSuccess() {
        val claims = jwtService.getAllClaimsFromToken(token)
        assertThat(claims).isNotNull
        assertThat(claims.subject).isEqualTo("user11")
        assertThat(claims["role"]).isNotNull
    }

    @Test
    fun testGetUsernameFromTokenSuccess() {
        val username = jwtService.getUsernameFromToken(token)
        assertThat(username).isNotNull()
        assertThat(username).isEqualTo("user11")
    }
}
