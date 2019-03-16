package dev.seshman.repository

import dev.seshman.domain.Item
import dev.seshman.domain.Session
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author vladov 2019-03-14
 */
interface SessionRepository:MongoRepository<Session,String>

interface ItemRepository:MongoRepository<Item,String>{
    fun findByDescription(description:String):List<Item>
    fun findByResult(result:String):List<Item>
}