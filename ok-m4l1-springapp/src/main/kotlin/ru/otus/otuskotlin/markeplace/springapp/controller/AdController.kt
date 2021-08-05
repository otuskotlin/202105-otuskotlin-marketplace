package ru.otus.otuskotlin.markeplace.springapp.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.markeplace.springapp.service.AdService
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.setQuery
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toCreateResponse
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdResponse
import ru.otus.otuskotlin.marketplace.openapi.models.ReadAdRequest

@RestController
@RequestMapping("/ad")
class AdController(
    private val adService: AdService
) {

    @PostMapping("create")
    fun createAd(@RequestBody createAdRequest: CreateAdRequest): CreateAdResponse {
        val context = MpContext().setQuery(createAdRequest)

        return adService.createAd(context).toCreateResponse()
    }

    @PostMapping("read")
    fun getAd(@RequestBody readAdRequest: ReadAdRequest) =
        MpContext().setQuery(readAdRequest).let {
            adService.getAd(it)
        }
}