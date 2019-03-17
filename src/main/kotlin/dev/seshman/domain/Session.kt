package dev.seshman.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

/**
 * @author vladov 2019-03-14
 */
@Document
data class Session(@Id val id: String?,
                   val date: LocalDate,
                   val description: String?,
                   val items: MutableList<Item> = mutableListOf())
