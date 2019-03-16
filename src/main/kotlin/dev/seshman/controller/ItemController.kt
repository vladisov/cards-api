package dev.seshman.controller

import dev.seshman.domain.Item
import dev.seshman.service.ItemService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

/**
 * @author vladov 2019-03-14
 */
@RestController("api/item")
class AuthorController(private val itemService: ItemService){

    @GetMapping
    fun getAll(pageable: Pageable): Page<Item> = itemService.getAll(pageable)

    @GetMapping("{desc}")
    fun getByDescription(@PathVariable desc:String): List<Item>
            = itemService.findByDescription(desc)

    @PostMapping
    fun saveItem(@RequestBody item: Item):
            Item = itemService.save(item)
}