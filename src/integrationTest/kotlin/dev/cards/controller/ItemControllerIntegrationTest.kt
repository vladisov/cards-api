package dev.cards.controller

import dev.cards.IntegrationTestBase
import dev.cards.domain.Item
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
    fun testCreateItemAndFindFirstSuccess() {
        val createdItem = createItem(Item(null, "content", "type", null, null), userId)
                .blockFirst()!!

        val uri = UriComponentsBuilder.fromPath("/api/item/first")
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
                    createdItem == item
                }
                .expectComplete()
                .verify()
    }
}
