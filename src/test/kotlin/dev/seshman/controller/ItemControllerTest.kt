package dev.seshman.controller

import com.google.gson.Gson
import dev.seshman.domain.Item
import dev.seshman.repository.ItemRepository
import dev.seshman.service.SessionService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


/**
 * @author vladov 2019-03-16
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(ItemController::class)
@EnableSpringDataWebSupport
class ItemControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockBean
    private lateinit var itemRepository: ItemRepository
    @MockBean
    private lateinit var sessionService: SessionService


    private lateinit var item: Item
    private lateinit var gson: Gson

    private val pageNumber = 1
    private val pageSize = 5

    @BeforeEach
    fun setup() {
        gson = Gson()
        item = Item("123", "desc", "res", "321")

        val pageRequest = PageRequest.of(pageNumber, pageSize)
        val pageItems = PageImpl(listOf(item))

        given(itemRepository.findAll(pageRequest)).willReturn(pageItems)
        given(sessionService.saveItem(item)).willReturn(item)
        given(itemRepository.findByDescriptionContaining("desc", pageRequest)).willReturn(pageItems)
    }

    @Test
    fun testGetAllItemsSuccess() {
        mockMvc.perform(get("api/item")
                .param("page", pageNumber.toString())
                .param("size", pageSize.toString())
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
                .content(gson.toJson(item))
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
                .param("page", pageNumber.toString())
                .param("size", pageSize.toString())
        )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.content[0].id").value("123"))
                .andExpect(jsonPath("$.content[0].description").value("desc"))
                .andExpect(jsonPath("$.content[0].result").value("res"))
                .andExpect(jsonPath("$.content[0].sessionId").value("321"))
    }
}

