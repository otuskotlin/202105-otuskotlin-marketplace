package ru.otus.otuskotlin.marketplace.backend.services

import marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.exceptions.MpOperationNotSet
import ru.otus.otuskotlin.marketplace.backend.common.models.IError
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.*
import ru.otus.otuskotlin.marketplace.openapi.models.*

class AdService {
    suspend fun createAd(context: MpContext, request: CreateAdRequest): CreateAdResponse {
        context.setQuery(request)
        context.responseAd = Bolt.getModel()
        return context.toCreateResponse()
    }

    suspend fun readAd(context: MpContext, request: ReadAdRequest): ReadAdResponse {
        context.setQuery(request)
        context.responseAd = Bolt.getModel()
        return context.toReadResponse()
    }

    suspend fun updateAd(context: MpContext, request: UpdateAdRequest): UpdateAdResponse {
        context.setQuery(request)
        context.responseAd = Bolt.getModel()
        return context.toUpdateResponse()
    }


    suspend fun deleteAd(context: MpContext, request: DeleteAdRequest): DeleteAdResponse {
        context.setQuery(request)
        context.responseAd = Bolt.getModel()
        return context.toDeleteResponse()
    }

    suspend fun searchAd(context: MpContext, request: SearchAdRequest): SearchAdResponse {
        context.setQuery(request)
        context.responseAds = Bolt.getModels().toMutableList()
        return context.toSearchResponse()
    }

    fun errorAd(context: MpContext, e: Throwable): BaseMessage {
        context.addError {
            from(e)
        }
        return context.toReadResponse()
    }

    fun initAd(context: MpContext, request: InitAdRequest): InitAdResponse {
        context.setQuery(request)
        return context.toInitResponse()
    }

    suspend fun offersAd(context: MpContext, request: OffersAdRequest): OffersAdResponse {
        context.setQuery(request)
        context.responseAds = Bolt.getModels().toMutableList()
        return context.toOffersResponse()
    }

}

