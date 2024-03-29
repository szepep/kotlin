package com.szepep.kotlin.service

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import kotlin.random.Random

@Service
class GeneratorImpl : Generator {

    override suspend fun generate(length: Int) = Random.nextString(length)

}