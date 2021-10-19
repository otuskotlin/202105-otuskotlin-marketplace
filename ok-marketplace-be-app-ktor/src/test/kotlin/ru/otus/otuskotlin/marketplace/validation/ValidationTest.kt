package ru.otus.otuskotlin.marketplace.validation

import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.configs.AppKtorConfig
import ru.otus.otuskotlin.marketplace.configs.KtorAuthConfig
import ru.otus.otuskotlin.marketplace.module
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdResponse
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ValidationTest {
    @Test
    fun `bad json`() {
        withTestApplication({
            module(config = AppKtorConfig())
        }) {
            handleRequest(HttpMethod.Post, "/ad/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8).toString())
                addHeader(HttpHeaders.Authorization, "Bearer ${KtorAuthConfig.testToken()}")
                setBody("{")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println("|$jsonString|")

                val res = Utils.mapper.readValue(response.content, CreateAdResponse::class.java)
                    ?: fail("Incorrect response format")

                assertEquals(CreateAdResponse.Result.ERROR, res.result)
                assertTrue {
                    res.errors?.find { it.message?.lowercase()?.contains("unexpected end-of-input") == true } != null
                }

            }
        }
    }
}
