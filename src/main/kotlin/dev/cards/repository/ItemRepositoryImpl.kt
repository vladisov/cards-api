package dev.cards.repository

import dev.cards.domain.Item
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import reactor.core.publisher.Flux


/**
 * @author vladov 19/01/2020
 */
class ItemRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : ItemRepositoryExt {
    override fun findRandomByUserId(userId: String): Flux<Item> {
        val aggregations = listOf(
                Aggregation.match(Criteria.where("userId").`is`(userId)),
                Aggregation.sample(1)
        )
        val aggregation = Aggregation.newAggregation(Item::class.java, aggregations)
        return this.mongoTemplate.aggregate(aggregation, Item::class.java)
    }
}
