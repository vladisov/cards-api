package dev.actions.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * @author vladov 2019-03-31
 */
@Configuration
@ConfigurationProperties(prefix = "jwtauth.password.encoder")
class EncoderProps {
    lateinit var secret: String
    var iteration: Int = 0
    var keylength: Int = 0
}
