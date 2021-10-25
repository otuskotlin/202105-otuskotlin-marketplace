package ru.otus.otuskotlin.marketplace.features

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.*
import ru.otus.otuskotlin.marketplace.configs.AppKtorConfig
import ru.otus.otuskotlin.marketplace.controllers.handleSession

fun Application.wsFeature(config: AppKtorConfig) {
    val userSessions = config.userSessions
    val objectMapper = config.objectMapper
    val adService = config.adService
    install(WebSockets)
    routing {
        webSocket("ws") {
            this.handleSession(objectMapper, adService, userSessions)
        }
    }
}
