package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdResponse
import java.time.Instant

suspend fun ApplicationCall.offersAd(adService: AdService) {
    val request = receive<OffersAdRequest>()
    val context = MpContext(
        startTime = Instant.now()
    )
    val result = try {
        adService.offersAd(context, request)
    } catch (e: Throwable) {
        adService.errorAd(context, e) as OffersAdResponse
    }
    respond(result)
}
