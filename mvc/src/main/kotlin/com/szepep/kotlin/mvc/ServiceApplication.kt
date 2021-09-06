package com.szepep.kotlin.mvc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.future.future
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.gildor.coroutines.okhttp.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ForkJoinPool

@SpringBootApplication
@EnableAsync
class ServiceApplication

fun main(args: Array<String>) {
    runApplication<ServiceApplication>(*args)
}

@RestController
class Controller(
    private val config: AppConfig
) {
    private companion object {
        private val HTTP_S = "^https?://".toRegex()
    }

    private val webClient = OkHttpClient.Builder().build().apply {
        dispatcher().maxRequestsPerHost = 100
    }

    @GetMapping("/data/{length}")
    fun getData(@PathVariable("length") length: Int): ResponseEntity<*> =
        webClient.newCall(buildRequest(length)).execute().asResponseEntity()


    @GetMapping("/suspend_data/{length}")
    suspend fun suspendGetData(@PathVariable("length") length: Int): ResponseEntity<*> =
        webClient.newCall(buildRequest(length)).await().asResponseEntity()


    @Async
    @GetMapping("/async_data/{length}")
    fun asyncGetData(@PathVariable("length") length: Int): CompletableFuture<ResponseEntity<*>> =
        CompletableFuture.completedFuture(
            webClient.newCall(buildRequest(length)).execute().asResponseEntity()
        )


    private val dispatcher = ForkJoinPool.commonPool().asCoroutineDispatcher()
    private val scope = CoroutineScope(dispatcher)

    @Async
    @GetMapping("/async_suspend_data/{length}")
    fun asyncSuspendGetData(@PathVariable("length") length: Int) = scope.future {
        webClient.newCall(buildRequest(length)).await().asResponseEntity()
    }

    private fun Response.asResponseEntity() = ResponseEntity.ok(body()?.bytes())


    private fun buildRequest(length: Int): Request {
        val apiHost = config.apiHost?.takeIf { it.matches(HTTP_S) } ?: "http://" + config.apiHost
        return Request.Builder()
            .get()
            .url("$apiHost/generate/$length/${config.responseTime}")
            .build()
    }

}

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app")
data class AppConfig(
    var apiHost: String? = null,
    var responseTime: Long = 0,
)