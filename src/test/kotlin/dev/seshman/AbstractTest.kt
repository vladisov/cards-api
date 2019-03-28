package dev.seshman

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * @author vladov 2019-03-17
 */
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
//@ContextConfiguration(classes = [TestConfiguration::class])
abstract class AbstractTest
