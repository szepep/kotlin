package com.szepep.kotlin.service

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import kotlinx.coroutines.delay
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.*
import java.net.URI

@SpringBootApplication
class ServiceApplication

fun main(args: Array<String>) {
    runApplication<ServiceApplication>(*args)
}

@Configuration
class SwaggerConfig {

    @Bean
    fun docsRouter() = router {
        GET("/") {
            ServerResponse.permanentRedirect(URI("/docs/swagger-ui.html")).build()
        }
    }
}

@Configuration
class RouterConfiguration {

    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/generate/{length}",
            operation = Operation(
                operationId = "generate",
                summary = "Generates output with length",
                parameters = [
                    Parameter(`in` = ParameterIn.PATH, name = "length", description = "the length of the result"),
                ]
            )
        ),
        RouterOperation(
            path = "/generate/{length}/{delay}",
            operation = Operation(
                operationId = "generate",
                summary = "Generates output with length and emulates delay",
                parameters = [
                    Parameter(`in` = ParameterIn.PATH, name = "length", description = "the length of the result"),
                    Parameter(`in` = ParameterIn.PATH, name = "delay", description = "emulates response time"),
                ]
            )
        ),
    )
    fun router(handler: RequestHandler) = coRouter {
        GET("/generate/{length}", handler::generate)
        GET("/generate/{length}/{delay}", handler::generate)
    }
}

@Service
class RequestHandler(
    private val generator: Generator
) {
    suspend fun generate(request: ServerRequest): ServerResponse {
        delay(request.delay())
        return ServerResponse.ok().bodyValueAndAwait(generator.generate(request.length()))
    }

    fun ServerRequest.length(): Int = pathVariable("length").toInt()
    fun ServerRequest.delay(): Long = pathVariables()["delay"]?.toLong() ?: 0
}

interface Generator {
    suspend fun generate(length: Int): String
}

