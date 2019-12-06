package dev.cards.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "card")
data class Item(@Id val id: String?,
                val header: String,
                val content: String,
                val type: String,
                var timestamp: Instant?,
                var userId: String?)
