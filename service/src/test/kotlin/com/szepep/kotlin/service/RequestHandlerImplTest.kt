package com.szepep.kotlin.service

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.text.CharSequenceLength
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

@SpringBootTest(
    classes = [
        RouterConfiguration::class,
        RequestHandlerImpl::class,
    ],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@EnableAutoConfiguration
internal class RequestHandlerImplTest: RestAssuredTestCase() {

    @LocalServerPort
    override lateinit var port: String

    @Test
    fun `test router with length 10`() {
        Given {
            spec(requestSpecification)
        } When {
            get("/generate/10/10")
        } Then {
            statusCode(HttpStatus.OK.value())
            body(CharSequenceLength(equalTo(10)))
        }
    }

    @Test
    fun `test router with length 100`() {
        Given {
            spec(requestSpecification)
        } When {
            get("/generate/10/100")
        } Then {
            statusCode(HttpStatus.OK.value())
            body(CharSequenceLength(equalTo(100)))
        }
    }
}