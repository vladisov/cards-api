package dev.seshman.service

import dev.seshman.AbstractTest
import dev.seshman.domain.Item
import dev.seshman.domain.Session
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable.unpaged
import java.time.LocalDate

/**
 * @author vladov 2019-03-17
 */
@SpringBootTest
internal class SessionServiceTest(@Autowired private val sessionService: SessionService) : AbstractTest() {

    @Test
    fun testSaveItemSuccess() {
        val sessionId = saveSession()
        val item = Item(null, "item_desc", "item_res", sessionId)

        sessionService.saveItem(item)

        val sessions = sessionService.findAll(unpaged())
        assertThat(sessions).isNotEmpty
    }

    private fun saveSession(): String {
        val session = sessionService.saveSession(Session(null, LocalDate.now(), "Session123"))
        return session.id!!
    }
}
