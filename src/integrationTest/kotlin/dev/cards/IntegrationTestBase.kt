package dev.cards

import dev.cards.domain.Item
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux

/**
 * @author vladov 19/01/2020
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

    lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setUp() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8085")
                .build();
    }

    protected fun createItem(item: Item, userId: String): Flux<Item> {
        val uri = UriComponentsBuilder.fromPath("/api/item")
                .queryParam("userId", userId)
                .build()
                .toUriString()
        return webTestClient
                .post()
                .uri(uri)
                .bodyValue(item)
                .exchange()
                .expectStatus().isOk
                .returnResult(Item::class.java)
                .responseBody
    }
}
