package dev.seshman.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

/**
 * @author vladov 2019-03-14
 */
@Document
data class Session(val date: LocalDate, val description: String, val items: Item)

@Document
data class Item(val description: String, val result: String)