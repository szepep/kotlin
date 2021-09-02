package com.szepep.kotlin.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

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

interface RequestHandler {
    suspend fun generate(request: ServerRequest): ServerResponse

    fun ServerRequest.delay(): Long = pathVariable("delay").toLong()
    fun ServerRequest.length(): Int = pathVariable("length").toInt()
}

