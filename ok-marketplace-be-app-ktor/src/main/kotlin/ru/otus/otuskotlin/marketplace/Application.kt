package ru.otus.otuskotlin.marketplace

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.AutoHeadResponse
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.controllers.KtorUserSession
import ru.otus.otuskotlin.marketplace.controllers.createAd
import ru.otus.otuskotlin.marketplace.controllers.deleteAd
import ru.otus.otuskotlin.marketplace.controllers.handleSession
import ru.otus.otuskotlin.marketplace.controllers.offersAd
import ru.otus.otuskotlin.marketplace.controllers.readAd
import ru.otus.otuskotlin.marketplace.controllers.searchAd
import ru.otus.otuskotlin.marketplace.controllers.updateAd
import ru.otus.otuskotlin.marketplace.logics.AdCrud

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// If you don't want to use config
object KtorEmbedded {
    @JvmStatic
    fun main(args: Array<String>) {
        io.ktor.server.engine.embeddedServer(Netty, port = 8000) {
            module()
        }.start(wait = true)
    }
}

@Suppress("UNUSED_PARAMETER") // Referenced in application.conf
@JvmOverloads
fun Application.module(config: AppKtorConfig = AppKtorConfig()) {
    val userSessions = config.userSessions
    val objectMapper = config.objectMapper
    val adService = config.adService

    install(DefaultHeaders)
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // TODO remove
    }
    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()
        }
    }
    install(AutoHeadResponse)
    // Generally not needed as it is replaced by a `routing`
    install(Routing)
    install(WebSockets)
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        // routing ad
        route("ad") {
            post("create") {
                call.createAd(adService)
            }
            post("read") {
                call.readAd(adService)
            }
            post("update") {
                call.updateAd(adService)
            }
            post("delete") {
                call.deleteAd(adService)
            }
            post("search") {
                call.searchAd(adService)
            }
        }
        // routing offers
        route("ad") {
            post("offers") {
                call.offersAd(adService)
            }
        }
        // Static feature. Try to access `/static/ktor-logo.png`
        static("static") {
            resources("static")
        }
        webSocket("ws") {
            this.handleSession(objectMapper, adService, userSessions)
        }
    }
}
