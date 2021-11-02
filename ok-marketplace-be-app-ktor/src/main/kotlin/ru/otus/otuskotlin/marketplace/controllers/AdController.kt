package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toCreateResponse
import ru.otus.otuskotlin.marketplace.logging.mpLogger
import ru.otus.otuskotlin.marketplace.openapi.models.*

suspend fun ApplicationCall.initAd(adService: AdService) = handleRequest<InitAdRequest, InitAdResponse>(
    "init-ad",
    mpLogger(this::class.java),
    { req -> adService.initAd(this, req) },
    { e -> adService.errorAd(this, e) as InitAdResponse }
)

suspend fun ApplicationCall.createAd(adService: AdService) {
    handleRequest<CreateAdRequest, CreateAdResponse>(
        "create-ad",
        mpLogger(this::createAd::class.java),
        { req -> adService.createAd(this, req) },
        { e -> addError(e); toCreateResponse() },
    )
}

suspend fun ApplicationCall.readAd(adService: AdService) {
    handleRequest<ReadAdRequest, ReadAdResponse>(
        "read-ad",
        mpLogger(this::readAd::class.java),
        { req -> adService.readAd(this, req) },
        { e -> adService.errorAd(this, e) as ReadAdResponse },
    )
}

suspend fun ApplicationCall.updateAd(adService: AdService) {
    handleRequest<UpdateAdRequest, UpdateAdResponse>(
        "update-ad",
        mpLogger(this::updateAd::class.java),
        { req -> adService.updateAd(this, req) },
        { e -> adService.errorAd(this, e) as UpdateAdResponse },
    )
}

suspend fun ApplicationCall.deleteAd(adService: AdService) {
    handleRequest<DeleteAdRequest, DeleteAdResponse>(
        "delete-ad",
        mpLogger(this::updateAd::class.java),
        { req -> adService.deleteAd(this, req) },
        { e -> adService.errorAd(this, e) as DeleteAdResponse },
    )
}

suspend fun ApplicationCall.searchAd(adService: AdService) {
    handleRequest<SearchAdRequest, SearchAdResponse>(
        "search-ad",
        mpLogger(this::searchAd::class.java),
        { req -> adService.searchAd(this, req) },
        { e -> adService.errorAd(this, e) as SearchAdResponse },
    )
}
