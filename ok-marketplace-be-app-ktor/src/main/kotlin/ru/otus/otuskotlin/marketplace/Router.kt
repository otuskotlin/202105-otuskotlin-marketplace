//package ru.otus.otuskotlin.marketplace
//
//import io.ktor.application.*
//import io.ktor.request.*
//import io.ktor.response.*
//import io.ktor.routing.*
//import ru.otus.otuskotlin.marketplace.backend.services.AdService
//import ru.otus.otuskotlin.marketplace.openapi.models.*
//import ru.otus.otuskotlin.marketplace.controllers.*
//
//fun Routing.ad(adService: AdService) = route("ad") {
//    post("create") {
//        call.createAd(adService)
//    }
//    post("read") {
//        call.readAd(adService)
//    }
//    post("update") {
//        call.updateAd(adService)
//    }
//    post("delete") {
//        call.deleteAd(adService)
//    }
//    post("search") {
//        call.searchAd(adService)
//    }
//}
//
//fun Routing.offers(adService: AdService) = route("ad") {
//    post("offers") {
//        call.offersAd(adService)
//    }
//}
