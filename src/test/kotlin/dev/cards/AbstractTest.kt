package dev.cards

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * @author vladov 2019-03-17
 */
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
abstract class AbstractTest
