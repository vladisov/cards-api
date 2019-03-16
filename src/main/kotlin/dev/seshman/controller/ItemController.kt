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

    @GetMapping(params = ["desc"])
    fun getByDescription(@RequestParam desc:String, pageable: Pageable): Page<Item>
            = itemService.findByDescription(desc, pageable)

    @PostMapping
    fun saveItem(@RequestBody item: Item):
            Item = itemService.save(item)
}