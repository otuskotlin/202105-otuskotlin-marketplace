package ru.otus.otuskotlin.marketplace

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.openapi.models.*
import ru.otus.otuskotlin.marketplace.controllers.*
import ru.otus.otuskotlin.marketplace.services.AdService
import ru.otus.otuskotlin.marketplace.services.OfferService

fun Routing.ad(adService: AdService) = route("ad") {
    post("init") {
        call.initAd(adService)
    }
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

fun Routing.offers(offerService: OfferService) = route("ad") {
    post("offers") {
        call.offersAd(offerService)
    }
}
