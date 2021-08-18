package ru.otus.otuskotlin.marketplace

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.openapi.models.*

fun Routing.ad() = route("ad") {
    post("init") {
        val data = call.receive<InitAdRequest>()
        call.respond(data)
    }
    post("create") {
        val data = call.receive<CreateAdRequest>()
        call.respond(data)
    }
    post("read") {
        val data = call.receive<ReadAdRequest>()
        call.respond(data)
    }
    post("update") {
        val data = call.receive<UpdateAdRequest>()
        call.respond(data)
    }
    post("delete") {
        val data = call.receive<DeleteAdRequest>()
        call.respond(data)
    }
    post("search") {
        val data = call.receive<SearchAdRequest>()
        call.respond(data)
    }
}

fun Routing.offers() = route("ad") {
    post("offers") {
        val data = call.receive<OffersAdRequest>()
        call.respond(data)
    }
}
