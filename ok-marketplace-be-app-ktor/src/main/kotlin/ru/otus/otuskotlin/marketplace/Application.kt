package ru.otus.otuskotlin.marketplace

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger
import ru.otus.otuskotlin.marketplace.services.AdServiceInterface
import ru.otus.otuskotlin.marketplace.services.OfferServiceInterface

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// If you don't want to use config
object KtorEmbedded {
    @JvmStatic
    fun main(args: Array<String>) {
//        io.ktor.server.engine.embeddedServer(Netty, commandLineEnvironment(args))
        io.ktor.server.engine.embeddedServer(Netty, port = 8000) {
            module()
        }.start(wait = true)
    }
}

@Suppress("UNUSED_PARAMETER") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(StatusPages) {
        exception(Exception::class.java) {
            data class ErrorResponse(val errors: Map<String, List<String?>>)
            val errorResponse = ErrorResponse(mapOf("error" to listOf("detail", this.toString())))
            context.respond(HttpStatusCode.InternalServerError, errorResponse)
        }
    }
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

    install(Koin) {
        slf4jLogger()
        modules(marketplace)
    }

    val adService by inject<AdServiceInterface>()
    val offerService by inject<OfferServiceInterface>()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        // routing ad
        ad(adService)
        // routing offers
        offers(offerService)
        // Static feature. Try to access `/static/ktor-logo.png`
        static("static") {
            resources("static")
        }
    }
}
