package dev.actions.service

import dev.actions.AbstractTest
import dev.actions.domain.Role
import dev.actions.domain.User
import dev.actions.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class UserServiceTest : AbstractTest() {

    @MockBean
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        userService = UserService(userRepository)
    }


    @Test
    fun testFindByUsernameSuccess() {
        given(userRepository.findByUsername("dummy")).willReturn(Mono.just(User("id", "dummy", "pw", true, listOf(Role.ROLE_USER))))

        val users = userService.findByUsername("dummy")
        StepVerifier
                .create(users)
                .assertNext { user ->
                    Assertions.assertThat(user).isNotNull
                    Assertions.assertThat(user.username).isEqualTo("dummy")
                    Assertions.assertThat(user.password).isEqualTo("pw")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByUsernameNotExist() {
        given(userRepository.findByUsername("dummy")).willReturn(Mono.empty())

        val items = userRepository.findByUsername("dummy")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }
}
