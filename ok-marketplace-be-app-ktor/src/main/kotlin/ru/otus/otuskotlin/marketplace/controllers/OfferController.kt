package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logging.mpLogger
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdResponse

suspend fun ApplicationCall.offersAd(adService: AdService) {
    handleRequest<OffersAdRequest, OffersAdResponse>(
        "offers-ad",
        mpLogger(ApplicationCall::offersAd::class.java),
        { req -> adService.offersAd(this, req) },
        { e -> adService.errorAd(this, e) as OffersAdResponse },
    )
}
