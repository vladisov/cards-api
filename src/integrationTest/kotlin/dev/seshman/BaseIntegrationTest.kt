package dev.seshman

import dev.seshman.domain.Item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.domain.Page
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("integrationtest")
class BaseIntegrationTest {

    val itemApi = "/api/item"

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun test_WhenSaveItem_Success() {
        val result = restTemplate.postForEntity(itemApi,
                Item("abc", "cda"), Item::class.java)
        assertNotNull(result)
        assertEquals(200, result.statusCodeValue)
        val item = result.body
        assertNotNull(item!!)
        assertEquals("abc", item.description)
        assertEquals("cda", item.result)
    }

    @Test
    fun test_WhenFindByDescription_Success() {
        saveDummyItem()

        val typeRef = ParameterizedTypeReference.forType<Page<Item>>(Page::class.java)
        val result = restTemplate.exchange("$itemApi?desc=dummy", HttpMethod.GET, buildRequest(), typeRef)

        assertNotNull(result)
        assertEquals(200, result.statusCodeValue)
        val items = result.body
    }

    private fun buildRequest(): HttpEntity<HashMap<String, String>> {
//        val requestParams = hashMapOf(Pair("desc",desc "dummy"))
        val requestHeaders = hashMapOf<String, String>()
        requestHeaders[HttpHeaders.CONTENT_TYPE] = "application/json;charset=UTF-8"
        return HttpEntity(requestHeaders)
    }

    private fun saveDummyItem() {
        restTemplate.postForEntity("/api/item", Item("dummy", "dummy"), Item::class.java)
    }
}
