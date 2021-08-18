package ru.otus.otuskotlin.marketplace

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.openapi.models.*
import ru.otus.otuskotlin.marketplace.controllers.*
import ru.otus.otuskotlin.marketplace.services.*

fun Routing.ad(adService: AdServiceInterface) = route("ad") {
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

fun Routing.offers(offerService: OfferServiceInterface) = route("ad") {
    post("offers") {
        call.offersAd(offerService)
    }
}
