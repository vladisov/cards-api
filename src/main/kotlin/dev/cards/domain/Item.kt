package dev.cards.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class Item(@Id val id: String?,
                val content: String,
                val type: String,
                val timestamp: Instant,
                var username: String?)
