package ru.otus.otuskotlin.marketplace

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.marketplace.configs.AppKtorConfig
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `root endpoint`() {
        withTestApplication({
            module(config = AppKtorConfig())
        }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello, world!", response.content)
            }
        }
    }
}
