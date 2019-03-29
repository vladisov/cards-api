package dev.seshman.repository

import dev.seshman.AbstractTest
import dev.seshman.domain.Item
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier
import java.time.LocalDateTime


/**
 * @author vladov 2019-03-16
 */
@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRepositoryTest(@Autowired private val itemRepository: ItemRepository) : AbstractTest() {

    @BeforeAll
    internal fun setup() {
        itemRepository.save(Item(null, "desc", "132sda", LocalDateTime.now())).block()
        itemRepository.save(Item(null, "addescda", "sda321", LocalDateTime.now())).block()
    }

    @Test
    fun testFindByDescriptionContainingSuccess() {
        val items = itemRepository.findByDescriptionContaining("desc")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.description).isEqualTo("desc")
                }
                .assertNext { item ->
                    assertThat(item.description).isEqualTo("addescda")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByResultSuccess() {
        val items = itemRepository.findByResultContaining("sda")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.result).isEqualTo("132sda")
                }
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.result).isEqualTo("sda321")
                }
                .expectComplete()
                .verify()
    }
}
