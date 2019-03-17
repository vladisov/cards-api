package dev.seshman.controller

import dev.seshman.domain.Item
import dev.seshman.repository.ItemRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * @author vladov 2019-03-14
 */
@RestController("api/item")
class ItemController(private val itemRepository: ItemRepository) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(pageable: Pageable): Page<Item> = itemRepository.findAll(pageable)

    @GetMapping(params = ["description"])
    fun getByDescription(@RequestParam description: String, pageable: Pageable):
            Page<Item> = itemRepository.findByDescriptionContaining(description, pageable)

    @PostMapping
    fun saveItem(@RequestBody item: Item): Item = itemRepository.save(item)
}
