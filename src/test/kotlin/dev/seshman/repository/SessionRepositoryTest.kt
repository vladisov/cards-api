package dev.seshman.repository

import dev.seshman.AbstractTest
import dev.seshman.domain.Item
import dev.seshman.domain.Session
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import java.time.LocalDate

/**
 * @author vladov 2019-03-17
 */
@DataMongoTest
class SessionRepositoryTest(@Autowired private val sessionRepository: SessionRepository) : AbstractTest() {

    @Test
    fun saveSessionWithItemsSuccess() {
        var session = sessionRepository.save(Session(null, LocalDate.now(), "Session #1"))
        session = sessionRepository.save(session)
        session.items.add(Item(
                null, "item1",
                "item1res",
                null))
        sessionRepository.save(session)
        assertThat(session.description).isEqualTo("Session #1")
        assertThat(session.items).hasSize(1)
    }
}
