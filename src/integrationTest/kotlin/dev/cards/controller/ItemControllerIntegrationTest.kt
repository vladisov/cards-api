package dev.cards.controller

import dev.cards.IntegrationTestBase
import dev.cards.domain.Item
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.web.util.UriComponentsBuilder
import reactor.test.StepVerifier


/**
 * @author vladov 19/01/2020
 */
class ItemControllerIntegrationTest : IntegrationTestBase() {

    private val userId = "dummyId"

    @Test
    @WithMockUser(username = "user", password = "pass")
    fun fetchAllItems() {
        val once = createItem(Item(null, "Once", "type1", null, null), userId).blockFirst()!!
        val upon = createItem(Item(null, "upon", "type2", null, null), userId).blockFirst()!!
        val aTime = createItem(Item(null, "a time", "type3", null, null), userId).blockFirst()!!
        val uri = UriComponentsBuilder
                .fromPath("/api/item/all")
                .queryParam("userId", userId)
                .build().toUriString()
        val responseBody = webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk
                .returnResult(Item::class.java)
                .responseBody
        StepVerifier
                .create(responseBody)
                .expectSubscription()
                .assertNext { item ->
                    assertThat(item.id).isEqualTo(once.id)
                    assertThat(item.timestamp).isNotNull()
                    assertThat(item.content).isEqualTo(once.content)
                    assertThat(item.type).isEqualTo(once.type)
                }
                .assertNext { item ->
                    assertThat(item.id).isEqualTo(upon.id)
                    assertThat(item.timestamp).isNotNull()
                    assertThat(item.content).isEqualTo(upon.content)
                    assertThat(item.type).isEqualTo(upon.type)
                }
                .assertNext { item ->
                    assertThat(item.id).isEqualTo(aTime.id)
                    assertThat(item.timestamp).isNotNull()
                    assertThat(item.content).isEqualTo(aTime.content)
                    assertThat(item.type).isEqualTo(aTime.type)
                }
                .thenCancel()
                .verify()
    }

    @Test
    @WithMockUser(username = "user", password = "pass")
    fun fetchRandomCardsSuccess() {
        val uri = UriComponentsBuilder.fromPath("/api/item/random")
                .queryParam("userId", userId)
                .build()
                .toUriString()
        val responseItem = webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk
                .returnResult(Item::class.java)
                .responseBody

        StepVerifier
                .create(responseItem)
                .expectSubscription()
                .assertNext { item ->
                    assertThat(item.id).isNotNull()
                }
                .expectComplete()
                .verify()

    }

    @Test
    @WithMockUser(username = "user", password = "pass")
    fun fetchItemByIdSuccess() {
        val createdItem = createItem(Item(null, "meaningful content", "type1", null, null), userId).blockFirst()!!
        val uri = UriComponentsBuilder
                .fromPath("/api/item")
                .queryParam("id", createdItem.id)
                .build().toUriString()
        val responseBody = webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk
                .returnResult(Item::class.java)
                .responseBody
        StepVerifier
                .create(responseBody)
                .expectSubscription()
                .assertNext { item ->
                    assertThat(item.id).isEqualTo(createdItem.id)
                    assertThat(item.timestamp).isNotNull()
                    assertThat(item.content).isEqualTo(createdItem.content)
                    assertThat(item.type).isEqualTo(createdItem.type)
                }
                .expectComplete()
                .verify()
    }

    @Test
    @WithMockUser(username = "user", password = "pass")
    fun fetchItemByContentSuccess() {
        val createdItem = createItem(Item(null, "content33", "type1", null, null), userId).blockFirst()!!
        val uri = UriComponentsBuilder
                .fromPath("/api/item")
                .queryParam("content", createdItem.content)
                .queryParam("userId", userId)
                .build().toUriString()
        val responseBody = webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk
                .returnResult(Item::class.java)
                .responseBody
        StepVerifier
                .create(responseBody)
                .expectSubscription()
                .assertNext { item ->
                    assertThat(item.id).isEqualTo(createdItem.id)
                    assertThat(item.timestamp).isNotNull()
                    assertThat(item.content).isEqualTo(createdItem.content)
                    assertThat(item.type).isEqualTo(createdItem.type)
                }
                .expectComplete()
                .verify()
    }

    @Test
    @WithMockUser(username = "user", password = "pass")
    fun fetchItemByTypeSuccess() {

        val createdItem = createItem(Item(null, "text", "type to search", null, null), userId).blockFirst()!!
        val uri = UriComponentsBuilder
                .fromPath("/api/item")
                .queryParam("type", createdItem.type)
                .queryParam("userId", userId)
                .build().toUriString()
        val responseBody = webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk
                .returnResult(Item::class.java)
                .responseBody
        StepVerifier
                .create(responseBody)
                .expectSubscription()
                .assertNext { item ->
                    assertThat(item.id).isEqualTo(createdItem.id)
                    assertThat(item.timestamp).isNotNull()
                    assertThat(item.content).isEqualTo(createdItem.content)
                    assertThat(item.type).isEqualTo(createdItem.type)
                }
                .expectComplete()
                .verify()
    }
}
