package ru.otus.otuskotlin.marketplace.features

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.configs.AppKtorConfig
import ru.otus.otuskotlin.marketplace.controllers.*

fun Application.restFeature(config: AppKtorConfig) {
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

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        // routing ad
        authenticate("auth-jwt") {
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
        }
        // Static feature. Try to access `/static/ktor-logo.png`
        static("static") {
            resources("static")
        }
    }
}
