package dev.seshman.repository

import dev.seshman.domain.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author vladov 2019-03-14
 */
interface ItemRepository:MongoRepository<Item,String>{
    fun findByDescriptionContaining(description: String, pageable: Pageable): Page<Item>
    fun findByResult(result:String):List<Item>
}
