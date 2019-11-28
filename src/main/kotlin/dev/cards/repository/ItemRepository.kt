package dev.cards.repository

import dev.cards.domain.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

/**
 * @author vladov 2019-03-14
 */
interface ItemRepository : ReactiveMongoRepository<Item, String> {
    fun findAllByUsername(username: String): Flux<Item>
    fun findByTypeAndId(type: String, userId: String): Flux<Item>
    fun findByContentContainingAndUsername(description: String, username: String): Flux<Item>
}
