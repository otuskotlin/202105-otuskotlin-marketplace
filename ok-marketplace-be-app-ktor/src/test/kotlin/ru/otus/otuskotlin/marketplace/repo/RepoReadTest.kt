package ru.otus.otuskotlin.marketplace.repo

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.marketplace.configs.AppKtorConfig
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.configs.KtorAuthConfig
import ru.otus.otuskotlin.marketplace.module
import ru.otus.otuskotlin.marketplace.openapi.models.*
import ru.otus.otuskotlin.marketplace.stubs.Bolt
import kotlin.test.assertEquals
import kotlin.test.fail

class RepoReadTest {
    @Test
    fun `read from db`() {
        val ad = Bolt.getModel()

        withTestApplication({
            val config = AppKtorConfig(
                adRepoTest = RepoAdInMemory(
                    initObjects = listOf(ad)
                )
            )
            module(config)
        }) {
            handleRequest(HttpMethod.Post, "/ad/read") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8).toString())
                addHeader(HttpHeaders.Authorization, "Bearer ${KtorAuthConfig.testUserToken()}")
                val request = ReadAdRequest(
                    readAdId = ad.id.asString(),
                    debug = BaseDebugRequest(mode = BaseDebugRequest.Mode.TEST)
                )
                val json = ObjectMapper().writeValueAsString(request)
                setBody(json)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println("|$jsonString|")

                val res = Utils.mapper.readValue(response.content, ReadAdResponse::class.java)
                    ?: fail("Incorrect response format")

                assertEquals(ReadAdResponse.Result.SUCCESS, res.result)
                assertEquals(ad.id.asString(), res.readAd?.id)
                assertEquals(AdDealSide.DEMAND, res.readAd?.dealSide)
                assertEquals(AdVisibility.PUBLIC, res.readAd?.visibility)
                assertEquals(ad.title, res.readAd?.title)
                assertEquals(ad.description, res.readAd?.description)
            }
        }
    }
}
