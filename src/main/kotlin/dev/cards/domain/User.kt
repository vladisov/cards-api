package dev.cards.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
data class User(@Id val id: String?,
                val username: String,
                val password: String,
                val enabled: Boolean,
                val roles: List<Role>)
