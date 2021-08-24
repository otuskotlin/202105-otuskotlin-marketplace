package ru.otus.otuskotlin.marketplace.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.openapi.models.*
import java.time.Instant

suspend fun ApplicationCall.initAd(adService: AdService) {
    val request = receive<InitAdRequest>()
    val context = MpContext(
        startTime = Instant.now()
    )
    val result = try {
            adService.initAd(context, request)
    } catch (e: Throwable) {
        adService.errorAd(context, e) as InitAdResponse
    }
    respond(result)
}

suspend fun ApplicationCall.createAd(adService: AdService) {
    val request = receive<CreateAdRequest>()
    val context = MpContext(
        startTime = Instant.now()
    )
    val result = try {
            adService.createAd(context, request)
    } catch (e: Throwable) {
        adService.errorAd(context, e) as CreateAdResponse
    }
    respond(result)
}

suspend fun ApplicationCall.readAd(adService: AdService) {
    val request = receive<ReadAdRequest>()
    val context = MpContext(
        startTime = Instant.now()
    )
    val result = try {
        adService.readAd(context, request)
    } catch (e: Throwable) {
        adService.errorAd(context, e) as ReadAdResponse
    }
    respond(result)
}

suspend fun ApplicationCall.updateAd(adService: AdService) {
    val request = receive<UpdateAdRequest>()
    val context = MpContext(
        startTime = Instant.now()
    )
    val result = try {
        adService.updateAd(context, request)
    } catch (e: Throwable) {
        adService.errorAd(context, e) as UpdateAdResponse
    }
    respond(result)
}

suspend fun ApplicationCall.deleteAd(adService: AdService) {
    val request = receive<DeleteAdRequest>()
    val context = MpContext(
        startTime = Instant.now()
    )
    val result = try {
        adService.deleteAd(context, request)
    } catch (e: Throwable) {
        adService.errorAd(context, e) as DeleteAdResponse
    }
    respond(result)
}

suspend fun ApplicationCall.searchAd(adService: AdService) {
    val request = receive<SearchAdRequest>()
    val context = MpContext(
        startTime = Instant.now()
    )
    val result = try {
        adService.searchAd(context, request)
    } catch (e: Throwable) {
        adService.errorAd(context, e) as SearchAdResponse
    }
    respond(result)
}
