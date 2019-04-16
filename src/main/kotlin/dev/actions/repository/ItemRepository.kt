package dev.actions.repository

import dev.actions.domain.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

/**
 * @author vladov 2019-03-14
 */
interface ItemRepository : ReactiveMongoRepository<Item, String> {
    fun findAllByUsername(username: String): Flux<Item>
    fun findByDescriptionContainingAndUsername(description: String, username: String): Flux<Item>
    fun findByResultContainingAndUsername(result: String, username: String): Flux<Item>
    fun findByTimestampBetweenAndUsernameOrderByTimestamp(startDate: LocalDateTime, endDate: LocalDateTime, username: String): Flux<Item>
}
