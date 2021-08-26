package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.module
import ru.otus.otuskotlin.marketplace.openapi.models.BaseMessage
import kotlin.test.assertEquals

abstract class RouterTest {
    protected inline fun <reified T> testPostRequest(
        body: BaseMessage? = null,
        uri: String,
        crossinline block: T.() -> Unit
    ) {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, uri) {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8).toString())
                setBody(Utils.mapper.writeValueAsString(body))
            }.apply {
                println(response.content)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())

                Utils.mapper.readValue(response.content, T::class.java).block()
            }
        }
    }
}