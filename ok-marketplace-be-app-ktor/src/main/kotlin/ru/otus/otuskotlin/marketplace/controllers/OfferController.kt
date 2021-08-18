package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.setQuery
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toOffersResponse
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest
import ru.otus.otuskotlin.marketplace.services.OfferServiceInterface

suspend fun ApplicationCall.offersAd(offerService: OfferServiceInterface) {
    val offersAdRequest = receive<OffersAdRequest>()
    respond(
        MpContext().setQuery(offersAdRequest).let {
            offerService.readOffers(it)
        }.toOffersResponse()
    )
}
