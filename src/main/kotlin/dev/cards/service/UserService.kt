package dev.cards.service

import dev.cards.domain.Role
import dev.cards.domain.User
import dev.cards.dto.UserDto
import dev.cards.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


/**
 * @author vladov 2019-03-31
 */
@Service
class UserService(private val userRepository: UserRepository) {

    fun findByUsername(username: String): Mono<UserDto> {
        return userRepository.findByUsername(username)
                .map { user ->
                    UserDto(user.id!!, user.username, user.password, true, listOf(Role.ROLE_USER))
                }
    }

    fun save(username: String, password: String): Mono<User> {
        return userRepository.save(User(null, username, password, true, listOf(Role.ROLE_USER)))
    }
}
