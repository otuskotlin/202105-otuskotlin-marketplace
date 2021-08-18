package ru.otus.otuskotlin.marketplace

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.openapi.models.*
import ru.otus.otuskotlin.marketplace.controllers.*

fun Routing.ad() = route("ad") {
    post("init") {
        call.initAd()
    }
    post("create") {
        call.createAd()
    }
    post("read") {
        call.readAd()
    }
    post("update") {
        call.updateAd()
    }
    post("delete") {
        call.deleteAd()
    }
    post("search") {
        call.searchAd()
    }
}

fun Routing.offers() = route("ad") {
    post("offers") {
        call.offersAd()
    }
}
