package dev.seshman.service

import dev.seshman.domain.Item
import dev.seshman.repository.ItemRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

/**
 * @author vladov 2019-03-14
 */
@Component
class ItemService(private val itemRepository: ItemRepository) {
    fun findByDescription(description: String, pageable: Pageable): Page<Item> {
        return itemRepository.findByDescription(description, pageable)
    }

    fun getAll(pageable: Pageable): Page<Item> {
        return itemRepository.findAll(pageable)
    }

    fun save(item: Item): Item {
        return itemRepository.save(item)
    }
}