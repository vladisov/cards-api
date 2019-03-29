package dev.seshman.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.seshman.domain.Item
import dev.seshman.repository.ItemRepository
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.time.LocalDateTime


/**
 * @author vladov 2019-03-16
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(ItemController::class)
@EnableSpringDataWebSupport
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class ItemControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockBean
    private lateinit var itemRepository: ItemRepository
    private lateinit var webClient: WebClient

    private lateinit var item: Item
    private lateinit var itemJson: String
    private lateinit var objectMapper: ObjectMapper


    @BeforeAll
    internal fun setup() {
        this.webClient = WebClient.create()

        item = Item("123", "desc", "res", LocalDateTime.now())
        objectMapper = ObjectMapper()
        itemJson = objectMapper.writeValueAsString(item)
        val itemFlux = Flux.just(item)

        given(itemRepository.findAll()).willReturn(itemFlux)
        given(itemRepository.findByDescriptionContaining("desc")).willReturn(itemFlux)
        given(itemRepository.findByResultContaining("res")).willReturn(itemFlux)
    }

    @Test
    fun testGetAllItemsSuccess() {
        mockMvc.perform(get("api/item")
        )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.content[0].id").value("123"))
                .andExpect(jsonPath("$.content[0].description").value("desc"))
                .andExpect(jsonPath("$.content[0].result").value("res"))
                .andExpect(jsonPath("$.content[0].sessionId").value("321"))
    }


    @Test
    fun testSaveItemSuccess() {
        mockMvc.perform(post("api/item")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(itemJson)
        )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.result").value("res"))
                .andExpect(jsonPath("$.sessionId").value("321"))
    }

    @Test
    fun testFindByDescriptionContainingSuccess() {
        mockMvc.perform(get("api/item")
                .param("description", item.description)
        )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.content[0].id").value("123"))
                .andExpect(jsonPath("$.content[0].description").value("desc"))
                .andExpect(jsonPath("$.content[0].result").value("res"))
                .andExpect(jsonPath("$.content[0].sessionId").value("321"))
    }

    @Test
    fun testFindByResultContainingSuccess() {
        mockMvc.perform(get("api/item")
                .param("result", item.result).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.content[0].id").value("123"))
                .andExpect(jsonPath("$.content[0].description").value("desc"))
                .andExpect(jsonPath("$.content[0].result").value("res"))
                .andExpect(jsonPath("$.content[0].sessionId").value("321"))
    }
}

