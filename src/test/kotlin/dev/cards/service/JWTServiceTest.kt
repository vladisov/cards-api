package dev.cards.service

import dev.cards.AbstractTest
import dev.cards.config.KeyProps
import dev.cards.domain.Role
import dev.cards.dto.UserDto
import io.jsonwebtoken.ExpiredJwtException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

class JWTServiceTest : AbstractTest() {

    private lateinit var keyProps: KeyProps
    private lateinit var jwtService: JWTService
    private val invalidToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiJ1c2VyMTEiLCJpYXQiOjE1NTQxMTIwODksImV4cCI6MTU1NDE0MDk2OX0.UBJ3bj966Xtmm57FzH4qJdbF1QIzCMYy-YE-9b1m_ZIElF8SnC4Wc59T0pKeXVRA0etkGcEcg5pyhHKDhEWI0Q"
    private val validToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiJ1c2VyIiwiaWF0IjoxNTU0MTQ1OTA0LCJleHAiOjkyNDY0NTg1MjAwfQ.9phRt48Qjsqgf12907wsL8HPSyoRe4HZLk2iWwnODmviXELGhAGsY6ltCaT89vqygildXOCXA37mxVNKq8tnTA"

    @BeforeEach
    fun setup() {
        keyProps = KeyProps()
        keyProps.secret = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave512bitsKeySize"
        keyProps.expiration = 100000
        jwtService = JWTService(keyProps)
    }

    @Test
    fun testGetExpirationDateFromTokenSuccess() {
        val dateFromToken = jwtService.getExpirationDateFromToken(validToken)
        assertThat(dateFromToken).isNotNull()
        assertThat(dateFromToken).isEqualTo(Date(92464585200000))
    }

    @Test
    fun generateTokenSuccess() {
        val generatedToken = jwtService.generateToken(UserDto("id", "user", "user", true, listOf(Role.ROLE_USER)))
        assertThat(generatedToken).isNotNull()
    }

    @Test
    fun testValidateTokenIsNotValid() {
        assertThrows(ExpiredJwtException::class.java) { jwtService.validateToken(invalidToken) }
    }

    @Test
    fun testGetAllClaimsFromTokenSuccess() {
        val claims = jwtService.getAllClaimsFromToken(validToken)
        assertThat(claims).isNotNull
        assertThat(claims.subject).isEqualTo("user")
        assertThat(claims["role"]).isNotNull
    }

    @Test
    fun testGetUsernameFromTokenSuccess() {
        val username = jwtService.getSubjectFromToken(validToken)
        assertThat(username).isNotNull()
        assertThat(username).isEqualTo("user")
    }
}
