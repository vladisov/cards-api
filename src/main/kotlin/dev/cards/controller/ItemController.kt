package dev.cards.controller

import dev.cards.domain.Item
import dev.cards.repository.ItemRepository
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

/**
 * @author vladov 2019-03-14
 */
@RestController("api/item")
@RequestMapping(path = ["api/item"])
class ItemController(private val itemRepository: ItemRepository) {

    @GetMapping(path = ["all"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("isAuthenticated()")
    fun findAll(@AuthenticationPrincipal userId: String): Flux<Item> {
        return itemRepository.findAllByUserId(userId)
    }

    @GetMapping(path = ["/first"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("isAuthenticated()")
    fun findFirst(@AuthenticationPrincipal userId: String): Flux<Item> {
        return itemRepository.findFirstByUserId(userId)
    }

    @GetMapping(params = ["id"])
    @PreAuthorize("isAuthenticated()")
    fun findById(@RequestParam id: String): Mono<Item> = itemRepository.findById(id)


    @GetMapping(params = ["content"])
    @PreAuthorize("isAuthenticated()")
    fun findByDescription(@RequestParam content: String, @AuthenticationPrincipal(expression = "id") username: String):
            Flux<Item> = itemRepository.findByContentContainingAndUserId(content, username)

    @GetMapping(params = ["type"])
    @PreAuthorize("isAuthenticated()")
    fun findByType(@RequestParam type: String, @AuthenticationPrincipal userId: String):
            Flux<Item> = itemRepository.findByTypeAndUserId(type, userId)


    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("isAuthenticated()")
    fun saveItem(@RequestBody item: Item, @AuthenticationPrincipal userId: String): Mono<Item> {
        item.userId = userId
        item.timestamp = Instant.now()
        return itemRepository.save(item)
    }
}
