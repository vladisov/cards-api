package dev.cards.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwtauth.jjwt")
class KeyProps {
    lateinit var secret: String
    var expiration: Long = 0
}
