package dev.seshman

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SeshmanApplication {
    fun main(args: Array<String>) {
        runApplication<SeshmanApplication>(*args)
    }
}


