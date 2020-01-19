package dev.cards.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.annotation.AuthenticationPrincipal
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux

/**
 * @author vladov 19/01/2020
 */
@Configuration
@EnableSwagger2WebFlux
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())

                .build()
                .globalOperationParameters(
                        listOf(ParameterBuilder()
                                .name("Authorization")
                                .description("JWT Authorization token")
                                .modelRef(ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()))
                .ignoredParameterTypes(AuthenticationPrincipal::class.java)
    }
}
