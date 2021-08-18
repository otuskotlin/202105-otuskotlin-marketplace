package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import ru.otus.otuskotlin.marketplace.openapi.models.*

suspend fun ApplicationCall.initAd() {
    val data = receive<InitAdRequest>()
    respond(data)
}

suspend fun ApplicationCall.createAd() {
    val data = receive<CreateAdRequest>()
    respond(data)
}

suspend fun ApplicationCall.readAd() {
    val data = receive<ReadAdRequest>()
    respond(data)
}

suspend fun ApplicationCall.updateAd() {
    val data = receive<UpdateAdRequest>()
    respond(data)
}

suspend fun ApplicationCall.deleteAd() {
    val data = receive<DeleteAdRequest>()
    respond(data)
}

suspend fun ApplicationCall.searchAd() {
    val data = receive<SearchAdRequest>()
    respond(data)
}
