package dev.seshman.service

import dev.seshman.domain.Item
import dev.seshman.domain.Session
import dev.seshman.repository.SessionRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author vladov 2019-03-17
 */
@Service
class SessionService(private val sessionRepository: SessionRepository) {

    companion object {
        private val logger = LoggerFactory.getLogger(SessionService::class.java)
    }

    fun saveSession(session: Session): Session {
        return sessionRepository.save(session)
    }

    fun saveItem(item: Item): Item {
        val sessionId = item.sessionId
        if (!sessionId.isNullOrBlank()) {
            val session = sessionRepository.findById(sessionId).unwrap()
            session?.let {
                session.items.add(item)
                sessionRepository.save(session)
                return item
            }.let {
                logger.error("session with id was not found")
            }
        }
        throw IllegalArgumentException("")
    }

    fun findAll(pageable: Pageable): Page<Session> {
        return sessionRepository.findAll(pageable)
    }

    private fun <T> Optional<T>.unwrap(): T? = orElse(null)
}
