package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.*
import ru.otus.otuskotlin.marketplace.openapi.models.*
import ru.otus.otuskotlin.marketplace.services.AdService

suspend fun ApplicationCall.initAd(adService: AdService) {
    val initAdRequest = receive<InitAdRequest>()
    respond(
        MpContext().setQuery(initAdRequest).let {
            adService.readAd(it)
        }.toInitResponse()
    )
}

suspend fun ApplicationCall.createAd(adService: AdService) {
    val readAdRequest = receive<CreateAdRequest>()
    respond(
        MpContext().setQuery(readAdRequest).let {
            adService.readAd(it)
        }.toReadResponse()
    )
}

suspend fun ApplicationCall.readAd(adService: AdService) {
    val readAdRequest = receive<ReadAdRequest>()
    respond(
        MpContext().setQuery(readAdRequest).let {
            adService.readAd(it)
        }.toReadResponse()
    )
}

suspend fun ApplicationCall.updateAd(adService: AdService) {
    val updateAdRequest = receive<UpdateAdRequest>()
    respond(
        MpContext().setQuery(updateAdRequest).let {
            adService.updateAd(it)
        }.toUpdateResponse()
    )
}

suspend fun ApplicationCall.deleteAd(adService: AdService) {
    val deleteAdRequest = receive<DeleteAdRequest>()
    respond(
        MpContext().setQuery(deleteAdRequest).let {
            adService.deleteAd(it).toDeleteResponse()
        }
    )
}

suspend fun ApplicationCall.searchAd(adService: AdService) {
    val searchAdRequest = receive<SearchAdRequest>()
    respond(
        MpContext().setQuery(searchAdRequest).let {
            adService.findAd(it)
        }.toSearchResponse()
    )
}
