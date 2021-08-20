package ru.otus.otuskotlin.markeplace.springapp.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.markeplace.springapp.service.OfferService
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.setQuery
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toOffersResponse
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest

@RestController
@RequestMapping("/ad")
class OfferController(
    private val offerService: OfferService
) {

    @PostMapping("offers")
    fun offersAd(@RequestBody offersAdRequest: OffersAdRequest) =
        MpContext().setQuery(offersAdRequest).let {
            offerService.readOffers(it)
        }.toOffersResponse()
}