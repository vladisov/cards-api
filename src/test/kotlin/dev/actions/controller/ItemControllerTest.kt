package dev.actions.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.actions.domain.Item
import dev.actions.repository.ItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec
import org.springframework.test.web.reactive.server.WebTestClient.ListBodySpec
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime


/**
 * @author vladov 2019-03-16
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(ItemController::class)
@EnableSpringDataWebSupport
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
//TODO needs investigation
class ItemControllerTest {

    @MockBean
    private lateinit var itemRepository: ItemRepository
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var webClient: WebTestClient
    private lateinit var item: Item
    private lateinit var itemJson: String


    @BeforeAll
    internal fun setup() {
        webClient = WebTestClient
                .bindToController(ItemController(itemRepository))
                .build()

        item = Item("123", "desc", "res", LocalDateTime.of(2019, 1, 1, 0, 0, 0))
        itemJson = objectMapper.writeValueAsString(item)
        val itemFlux = Flux.just(item)

        given(itemRepository.save(item)).willReturn(Mono.just(item))
        given(itemRepository.findAll()).willReturn(itemFlux)
        given(itemRepository.findByDescriptionContaining("desc")).willReturn(itemFlux)
        given(itemRepository.findByResultContaining("res")).willReturn(itemFlux)
    }

    @Test
    fun testGetAllItemsSuccess() {
        val result: FluxExchangeResult<Item> = webClient.get().uri("/api/item")
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(Item::class.java)

        val itemsFlux: Flux<Item> = result.responseBody
        StepVerifier
                .create(itemsFlux)
                .expectSubscription()
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.id).isEqualTo("123")
                    assertThat(item.description).isEqualTo("desc")
                    assertThat(item.result).isEqualTo("res")
                }
                .expectComplete()
                .verify()
    }


    @Test
    fun testSaveItemSuccess() {
        val itemBodySpec: BodySpec<Item, *> = webClient.post()
                .uri("/api/item")
                .syncBody(item)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Item::class.java)
        itemBodySpec.consumeWith<Nothing> { saved ->
            assertThat(saved).isNotNull
        }
    }

    @Test
    fun testFindByDescriptionContainingSuccess() {
        val itemsList: ListBodySpec<Item> = webClient.get()
                .uri("/api/item")
                .attribute("description", item.description)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item::class.java)
        itemsList.consumeWith<ListBodySpec<Item>> { saved ->
            assertThat(saved).isNotNull
//            assertThat(saved.responseBody!!).isEqualTo("123")
        }
    }

    @Test
    fun testFindByResultContainingSuccess() {
        val itemsList: ListBodySpec<Item> = webClient.get()
                .uri("/api/item")
                .attribute("result", item.result)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item::class.java)
        itemsList.consumeWith<ListBodySpec<Item>> { saved ->
            assertThat(saved).isNotNull
//            assertThat(item.id).isEqualTo("123")
        }
    }
}

