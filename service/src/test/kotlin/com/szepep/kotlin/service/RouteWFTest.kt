package com.szepep.kotlin.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebFluxTest
@Import(
    RouterConfiguration::class,
    RequestHandler::class,
    GeneratorImpl::class,
)
internal class RouteWFTest {

    @Autowired
    private lateinit var client: WebTestClient

    @Test
    fun `test router with length 10`() {
        client.get().uri("/generate/10/10").exchange()
            .expectStatus().isOk
            .expectBody<String>().consumeWith { assertEquals(10, it.responseBody?.length) }
    }

    @Test
    fun `test router with length 100`() {
        client.get().uri("/generate/100/10").exchange()
            .expectStatus().isOk
            .expectBody<String>().consumeWith { assertEquals(100, it.responseBody?.length) }
    }

    @Test
    fun `test router with length 100 and no delay`() {
        client.get().uri("/generate/100").exchange()
            .expectStatus().isOk
            .expectBody<String>().consumeWith { assertEquals(100, it.responseBody?.length) }
    }
}