package dev.actions.repository

import dev.actions.AbstractTest
import dev.actions.domain.Item
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
        itemRepository.save(Item(null, "desc", "132sda", LocalDateTime.now(), "username")).block()
        itemRepository.save(Item(null, "addescda", "sda321", LocalDateTime.now(), "username")).block()
        itemRepository.save(Item(null, "dasda", "das", LocalDateTime.now(), "username111")).block()
    }

    @Test
    fun testFindByDescriptionContainingAndUsernameSuccess() {
        val items = itemRepository.findByDescriptionContainingAndUsername("desc", "username")
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
    fun testFindByDescriptionContainingAndUsernameEmptyResult() {
        val items = itemRepository.findByDescriptionContainingAndUsername("desc", "vasya")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByResultContainingAndUsernameSuccess() {
        val items = itemRepository.findByDescriptionContainingAndUsername("desc", "username")
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
    fun testFindByResultContainingAndUsernameEmptyResult() {
        val items = itemRepository.findByResultContainingAndUsername("desc", "vasya")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }
}
