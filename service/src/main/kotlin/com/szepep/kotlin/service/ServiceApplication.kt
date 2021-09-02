package com.szepep.kotlin.service

import kotlinx.coroutines.delay
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.*

@SpringBootApplication
class ServiceApplication

fun main(args: Array<String>) {
    runApplication<ServiceApplication>(*args)
}

@Configuration
class RouterConfiguration {

    @Bean
    fun router(handler: RequestHandler) = coRouter {
        GET("/generate/{delay}/{length}", handler::generate)
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

    fun ServerRequest.delay(): Long = pathVariable("delay").toLong()
    fun ServerRequest.length(): Int = pathVariable("length").toInt()
}

interface Generator {
    suspend fun generate(length: Int): String
}

