package dev.cards.repository

import dev.cards.AbstractTest
import dev.cards.domain.Role
import dev.cards.domain.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest(@Autowired private val userRepository: UserRepository) : AbstractTest() {

    @BeforeAll
    internal fun setup() {
        userRepository.save(User(null, "vasya", "pw", true, listOf(Role.ROLE_USER))).block()
        userRepository.save(User(null, "petya", "pw1", true, listOf(Role.ROLE_USER))).block()
    }

    @Test
    fun testFindByUsernameSuccess() {
        val items = userRepository.findByUsername("vasya")
        StepVerifier
                .create(items)
                .assertNext { user ->
                    Assertions.assertThat(user).isNotNull
                    Assertions.assertThat(user.username).isEqualTo("vasya")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByUsernameNotExist() {
        val items = userRepository.findByUsername("aada")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }
}
