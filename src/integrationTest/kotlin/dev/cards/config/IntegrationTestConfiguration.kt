package dev.cards.config

import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * @author vladov 19/01/2020
 */
@EnableAutoConfiguration(exclude = [
    SecurityAutoConfiguration::class,
    SecurityFilterAutoConfiguration::class,
    ReactiveSecurityAutoConfiguration::class,
    SecurityFilterAutoConfiguration::class,
    ManagementWebSecurityAutoConfiguration::class])
@Configuration
@Profile("test")
class IntegrationTestConfiguration
