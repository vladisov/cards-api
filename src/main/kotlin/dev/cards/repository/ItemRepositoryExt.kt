package dev.cards.repository

import dev.cards.domain.Item
import reactor.core.publisher.Flux

/**
 * @author vladov 19/01/2020
 */
interface ItemRepositoryExt {
    fun findRandomByUserId(userId: String): Flux<Item>
}
