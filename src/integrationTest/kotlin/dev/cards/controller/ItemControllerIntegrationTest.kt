package dev.cards.controller

import dev.cards.CardsApplication
import dev.cards.config.WebSecurityConfig
import dev.cards.domain.Item
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.test.StepVerifier


/**
 * @author vladov 19/01/2020
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ContextConfiguration(classes = [CardsApplication::class, WebSecurityConfig::class])
@EnableAutoConfiguration(exclude = [
    SecurityAutoConfiguration::class,
    SecurityFilterAutoConfiguration::class,
    ReactiveSecurityAutoConfiguration::class,
    SecurityFilterAutoConfiguration::class,
    ManagementWebSecurityAutoConfiguration::class])
class ItemControllerIntegrationTest {

    lateinit var webTestClient: WebTestClient

    private val userId = "dummyId"

    @BeforeEach
    fun setUp() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8085")
                .build();
    }

    @Test
    @WithMockUser(username = "user", password = "pass")
    fun test() {
        val createdItem = createItem().blockFirst()!!

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

    fun createItem(): Flux<Item> {
        val uri = UriComponentsBuilder.fromPath("/api/item")
                .queryParam("userId", userId)
                .build()
                .toUriString()
        return webTestClient
                .post()
                .uri(uri)
                .bodyValue(Item(null, "content", "type", null, null))
                .exchange()
                .expectStatus().isOk
                .returnResult(Item::class.java)
                .responseBody
    }
}
