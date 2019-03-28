package dev.seshman.repository

import dev.seshman.AbstractTest
import dev.seshman.domain.Item
import dev.seshman.domain.Session
import dev.seshman.service.SessionService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import java.time.LocalDate

/**
 * @author vladov 2019-03-16
 */
@SpringBootTest
class ItemRepositoryTest(
        @Autowired private val itemRepository: ItemRepository,
        @Autowired private val sessionService: SessionService) : AbstractTest() {

    @BeforeEach
    fun setup() {
        val session = sessionService.saveSession(Session(null, LocalDate.now(), ""))
        val item1 = itemRepository.save(Item(null, "desc", "sda", session.id))
        val item2 = itemRepository.save(Item(null, "addescda", "sda", session.id))
        sessionService.saveItem(item1)
        sessionService.saveItem(item2)
    }

    @Test
    fun testGetByDescriptionContainingSuccess() {
        val items = itemRepository.findByDescriptionContaining("desc", Pageable.unpaged())
        assertThat(items).hasSize(2)
    }
}
