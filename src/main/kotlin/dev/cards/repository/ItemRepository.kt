package dev.cards.repository

import dev.cards.domain.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

/**
 * @author vladov 2019-03-14
 */
interface ItemRepository : ReactiveMongoRepository<Item, String>, ItemRepositoryExt {
    fun findAllByUserId(userId: String): Flux<Item>
    fun findByTypeAndUserId(type: String, userId: String): Flux<Item>
    fun findByContentContainingAndUserId(content: String, userId: String): Flux<Item>
}
