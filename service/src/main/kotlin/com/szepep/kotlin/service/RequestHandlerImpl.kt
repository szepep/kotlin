package com.szepep.kotlin.service

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import kotlin.random.Random

@Service
class RequestHandlerImpl: RequestHandler {
    override suspend fun generate(request: ServerRequest): ServerResponse {
        val delay: Long = request.delay()
        val length: Int = request.length()

        delay(delay)
        return ServerResponse.ok().bodyValueAndAwait(Random.nextString(length))
    }
}