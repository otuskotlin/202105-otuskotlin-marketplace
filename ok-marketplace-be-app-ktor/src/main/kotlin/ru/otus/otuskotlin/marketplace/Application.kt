package ru.otus.otuskotlin.marketplace

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*

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
fun Application.module(testing: Boolean = false) {
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
    install(ContentNegotiation)
    // Generally not needed as it is replaced by a `routing`
    install(Routing)
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }
}
