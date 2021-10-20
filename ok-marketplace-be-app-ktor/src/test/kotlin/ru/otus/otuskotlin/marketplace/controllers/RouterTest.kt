package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.http.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.configs.AppKtorConfig
import ru.otus.otuskotlin.marketplace.configs.KtorAuthConfig
import ru.otus.otuskotlin.marketplace.module
import ru.otus.otuskotlin.marketplace.openapi.models.BaseMessage
import kotlin.test.assertEquals

abstract class RouterTest {
    protected inline fun <reified T> testPostRequest(
        body: BaseMessage? = null,
        uri: String,
        config: AppKtorConfig = AppKtorConfig(),
        result: HttpStatusCode = HttpStatusCode.OK,
        crossinline block: T.() -> Unit = {}
    ) {
        withTestApplication({
            module(config = config)
        }) {
            handleRequest(HttpMethod.Post, uri) {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8).toString())
                addHeader(HttpHeaders.Authorization, "Bearer ${KtorAuthConfig.testToken()}")
                setBody(Utils.mapper.writeValueAsString(body))
            }.apply {
                println(response.content)
                assertEquals(result, response.status())
                if (result == HttpStatusCode.OK) {
                    assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                    Utils.mapper.readValue(response.content, T::class.java).block()
                }
            }
        }
    }
}
