package dev.actions.controller

import dev.actions.domain.Item
import dev.actions.repository.ItemRepository
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

/**
 * @author vladov 2019-03-14
 */
@RestController("api/item")
class ItemController(private val itemRepository: ItemRepository) {


    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun findAll(@AuthenticationPrincipal username: String): Flux<Item> {
        return itemRepository.findAllByUsername(username)
    }

    @GetMapping(params = ["id"])
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun findById(@RequestParam id: String): Mono<Item> = itemRepository.findById(id)

    @GetMapping(params = ["date"])
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun findById(@RequestParam date: LocalDate, @AuthenticationPrincipal username: String): Flux<Item> {
        val startDate = date.atStartOfDay()
        val endDate = date.plusDays(1).atStartOfDay()
        return itemRepository.findByTimestampBetweenAndUsernameOrderByTimestamp(startDate, endDate, username)
    }

    @GetMapping(params = ["description"])
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun findByDescription(@RequestParam description: String, @AuthenticationPrincipal username: String):
            Flux<Item> = itemRepository.findByDescriptionContainingAndUsername(description, username)

    @GetMapping(params = ["result"])
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun findByResult(@RequestParam result: String, @AuthenticationPrincipal username: String):
            Flux<Item> = itemRepository.findByResultContainingAndUsername(result, username)

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun saveItem(@RequestBody item: Item, @AuthenticationPrincipal username: String): Mono<Item> {
        item.username = username
        return itemRepository.save(item)
    }
}
