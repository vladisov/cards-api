package dev.seshman.repository

import dev.seshman.domain.Session
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author vladov 2019-03-16
 */
interface SessionRepository : MongoRepository<Session, String>
