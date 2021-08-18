package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest

suspend fun ApplicationCall.offersAd() {
    val data = receive<OffersAdRequest>()
    respond(data)
}
