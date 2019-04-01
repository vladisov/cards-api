package dev.actions.repository

import dev.actions.domain.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

/**
 * @author vladov 2019-03-14
 */
interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUsername(username: String): Mono<User>
}
