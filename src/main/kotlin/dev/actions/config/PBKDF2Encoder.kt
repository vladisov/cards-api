package dev.actions.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


/**
 * @author vladov 2019-03-30
 */
@Component
@EnableConfigurationProperties(EncoderProps::class)
class PBKDF2Encoder : PasswordEncoder {

    @Autowired
    private lateinit var encoderProps: EncoderProps

    override fun encode(cs: CharSequence): String {
        try {
            val secret = encoderProps.secret
            val keylength = encoderProps.keylength
            val iteration = encoderProps.iteration

            val result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(PBEKeySpec(cs.toString().toCharArray(), secret.toByteArray(), iteration, keylength))
                    .encoded
            return Base64.getEncoder().encodeToString(result)
        } catch (ex: NoSuchAlgorithmException) {
            throw RuntimeException(ex)
        } catch (ex: InvalidKeySpecException) {
            throw RuntimeException(ex)
        }
    }

    override fun matches(cs: CharSequence, encodedPassword: String): Boolean {
        return encode(cs) == encodedPassword
    }
}
