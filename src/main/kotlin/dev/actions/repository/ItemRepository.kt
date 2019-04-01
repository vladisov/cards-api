package dev.actions.repository

import dev.actions.domain.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

/**
 * @author vladov 2019-03-14
 */
interface ItemRepository : ReactiveMongoRepository<Item, String> {
    fun findByDescriptionContaining(description: String): Flux<Item>
    fun findByResultContaining(result: String): Flux<Item>
}
