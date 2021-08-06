package ru.otus.otuskotlin.markeplace.springapp.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.springapp.service.AdService
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.*
import ru.otus.otuskotlin.marketplace.openapi.models.*

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
        }.toReadResponse()

    @RequestMapping("update", method = [RequestMethod.POST])
    fun updateAd(@RequestBody updateAdRequest: UpdateAdRequest): UpdateAdResponse {
        return MpContext().setQuery(updateAdRequest).let {
            adService.updateAd(it)
        }.toUpdateResponse()
    }

    @PostMapping("delete")
    fun deleteAd(@RequestBody deleteAdRequest: DeleteAdRequest): DeleteAdResponse {
        val context = MpContext().setQuery(deleteAdRequest)

        val result = adService.deleteAd(context)

        return result.toDeleteResponse()
    }

    @PostMapping("search")
    fun searchAd(@RequestBody searchAdRequest: SearchAdRequest) =
        MpContext().setQuery(searchAdRequest).let {
            adService.findAd(it)
        }.toSearchResponse()

    @PostMapping("offers")
    fun getOffers(@RequestBody offersAdRequest: OffersAdRequest) =
        MpContext().setQuery(offersAdRequest).let {
            adService.getOffers(it)
        }.toOffersResponse()
}