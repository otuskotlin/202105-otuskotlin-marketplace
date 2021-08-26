package ru.otus.otuskotlin.markeplace.springapp.controllers

import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdResponse
import java.time.Instant

@RestController
@RequestMapping("/ad")
class OfferController(
    private val service: AdService
) {

    @PostMapping("offers")
    fun createAd(@RequestBody request: OffersAdRequest): OffersAdResponse {
        val context = MpContext(
            startTime = Instant.now()
        )
        return try {
            runBlocking {
                service.offersAd(context, request)
            }
        } catch (e: Throwable) {
            return service.errorAd(context, e) as OffersAdResponse
        }
    }

}
