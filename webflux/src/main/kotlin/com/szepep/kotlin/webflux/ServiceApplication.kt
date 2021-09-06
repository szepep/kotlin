package com.szepep.kotlin.webflux

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange

@SpringBootApplication
class ServiceApplication

fun main(args: Array<String>) {
    runApplication<ServiceApplication>(*args)
}

@RestController
class Controller(
    private val config: AppConfig
) {

    private val webClient = WebClient.create()

    @GetMapping("/data/{length}")
    suspend fun getData(@PathVariable("length") length: Int): ResponseEntity<*> {
        return webClient.get()
            .uri("${config.apiHost}/generate/$length/${config.responseTime}")
            .exchangeToMono { it.toEntity(ByteArray::class.java) }
            .awaitSingle()
    }
}

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app")
data class AppConfig(
    var apiHost: String? = null,
    var responseTime: Long = 0,
)