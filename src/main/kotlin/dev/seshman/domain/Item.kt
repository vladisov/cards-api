package dev.seshman.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Item(@Id val id: String?,
                val description: String = "",
                val result: String = "",
                val timestamp: LocalDateTime)
