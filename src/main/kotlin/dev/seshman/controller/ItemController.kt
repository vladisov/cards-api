package dev.seshman.controller

import dev.seshman.domain.Item
import dev.seshman.repository.ItemRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author vladov 2019-03-14
 */
@RestController("api/item")
class ItemController(private val itemRepository: ItemRepository) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAll(): Flux<Item> = itemRepository.findAll()

    @GetMapping(params = ["description"])
    fun findByDescription(@RequestParam description: String): Flux<Item> = itemRepository.findByDescriptionContaining(description)

    @GetMapping(params = ["result"])
    fun findByResult(@RequestParam result: String): Flux<Item> = itemRepository.findByResultContaining(result)

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun saveItem(@RequestBody item: Item): Mono<Item> = itemRepository.save(item)
}
