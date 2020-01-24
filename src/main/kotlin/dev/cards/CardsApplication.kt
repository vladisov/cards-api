package dev.cards

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableEncryptableProperties
class CardsApplication

fun main(args: Array<String>) {
    runApplication<CardsApplication>(*args)
}


