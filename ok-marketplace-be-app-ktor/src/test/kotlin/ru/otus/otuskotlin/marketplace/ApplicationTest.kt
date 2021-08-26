package ru.otus.otuskotlin.marketplace

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `root endpoint`() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello, world!", response.content)
            }
        }
    }
}