package dev.actions.service

import dev.actions.domain.User
import dev.actions.dto.Role
import dev.actions.dto.UserDto
import dev.actions.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*


/**
 * @author vladov 2019-03-31
 */
@Service
class UserService(private val userRepository: UserRepository) {

    //username:password -> user:user
    private val userUsername = "user"// password: user
    private val user = UserDto(userUsername, "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=",
            true, Arrays.asList(Role.ROLE_USER))

    //    username:password -> admin:admin
    private val adminUsername = "admin"// password: admin
    private val admin = UserDto(adminUsername, "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=",
            true, Arrays.asList(Role.ROLE_ADMIN))

    fun findByUsername(username: String): Mono<UserDto> {
        return userRepository.findByUsername(username)
                .map { user ->
                    UserDto(user.username, user.password, true, Arrays.asList(Role.ROLE_USER))
                }
    }

    fun save(username: String, password: String): Mono<User> {
        return userRepository.save(User(null, username, password))
    }
}
