package ru.otus.otuskotlin.marketplace.backend.services

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.*
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import ru.otus.otuskotlin.marketplace.openapi.models.*

class AdService(
    private val crud: AdCrud,
) {
    suspend fun createAd(context: MpContext, request: CreateAdRequest): CreateAdResponse {
        crud.create(context.setQuery(request))
        return context.toCreateResponse()
    }

    suspend fun readAd(context: MpContext, request: ReadAdRequest): ReadAdResponse {
        crud.read(context.setQuery(request))
        return context.toReadResponse()
    }

    suspend fun updateAd(context: MpContext, request: UpdateAdRequest): UpdateAdResponse {
        crud.update(context.setQuery(request))
        return context.toUpdateResponse()
    }


    suspend fun deleteAd(context: MpContext, request: DeleteAdRequest): DeleteAdResponse {
        crud.delete(context.setQuery(request))
        return context.toDeleteResponse()
    }

    suspend fun searchAd(context: MpContext, request: SearchAdRequest): SearchAdResponse {
        crud.search(context.setQuery(request))
        return context.toSearchResponse()
    }

    fun errorAd(context: MpContext, e: Throwable): BaseMessage {
        context.addError(e)
        return context.toReadResponse()
    }

    fun initAd(context: MpContext, request: InitAdRequest): InitAdResponse {
        context.setQuery(request)
        return context.toInitResponse()
    }

    fun finishAd(context: MpContext) {
        // TODO handle user disconnection
    }

    suspend fun offersAd(context: MpContext, request: OffersAdRequest): OffersAdResponse {
        crud.offer(context.setQuery(request))
        return context.toOffersResponse()
    }
}

