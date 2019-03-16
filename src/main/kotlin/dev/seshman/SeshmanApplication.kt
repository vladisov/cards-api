package dev.seshman

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile

@SpringBootApplication
class SeshmanApplication

fun main(args: Array<String>) {
	runApplication<SeshmanApplication>(*args)
}
