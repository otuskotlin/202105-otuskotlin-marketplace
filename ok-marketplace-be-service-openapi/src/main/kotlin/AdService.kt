package ru.otus.otuskotlin.marketplace.backend.services

import org.slf4j.event.Level
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.services.exceptions.DataNotAllowedException
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.*
import ru.otus.otuskotlin.marketplace.logging.mpLogger
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import ru.otus.otuskotlin.marketplace.openapi.models.*

class AdService(private val crud: AdCrud) {
    private val logger = mpLogger(this::class.java)

    suspend fun handleAd(context: MpContext, request: BaseMessage): BaseMessage = try {
        when (request) {
            is InitAdRequest -> initAd(context, request)
            is CreateAdRequest -> createAd(context, request)
            is ReadAdRequest -> readAd(context, request)
            is UpdateAdRequest -> updateAd(context, request)
            is DeleteAdRequest -> deleteAd(context, request)
            is SearchAdRequest -> searchAd(context, request)
            is OffersAdRequest -> offersAd(context, request)
            else -> throw DataNotAllowedException("Request is not Allowed", request)
        }
    } catch (e: Throwable) {
        errorAd(context, e)
    }

    suspend fun createAd(context: MpContext, request: CreateAdRequest): CreateAdResponse {
        context.setQuery(request)
        return context.handle("create-ad", MpContext::toCreateResponse) {
            crud.create(it)
        }
    }

    suspend fun readAd(context: MpContext, request: ReadAdRequest): ReadAdResponse {
        context.setQuery(request)
        return context.handle("read-ad", MpContext::toReadResponse) {
            crud.read(it)
        }
    }

    suspend fun updateAd(context: MpContext, request: UpdateAdRequest): UpdateAdResponse {
        context.setQuery(request)
        return context.handle("update-ad", MpContext::toUpdateResponse) {
            crud.update(it)
        }
    }

    suspend fun deleteAd(context: MpContext, request: DeleteAdRequest): DeleteAdResponse {
        context.setQuery(request)
        return context.handle("delete-ad", MpContext::toDeleteResponse) {
            crud.delete(it)
        }
    }

    suspend fun searchAd(context: MpContext, request: SearchAdRequest): SearchAdResponse {
        context.setQuery(request)
        return context.handle("search-ad", MpContext::toSearchResponse) {
            crud.search(it)
        }
    }

    suspend fun errorAd(context: MpContext, e: Throwable): BaseMessage {
        context.addError(e)
        return context.handle("error-ad", MpContext::toReadResponse)
    }

    suspend fun initAd(context: MpContext, request: InitAdRequest): InitAdResponse {
        context.setQuery(request)
        return context.handle("init-ad", MpContext::toInitResponse)
    }

    @Suppress("UNUSED_PARAMETER", "RedundantSuspendModifier")
    suspend fun finishAd(context: MpContext) {
        // TODO handle user disconnection
    }

    suspend fun offersAd(context: MpContext, request: OffersAdRequest): OffersAdResponse {
        context.setQuery(request)
        return context.handle("offers-ad", MpContext::toOffersResponse) {
            crud.offer(it)
        }
    }

    private suspend fun <T> MpContext.handle(
        logId: String,
        mapper: MpContext.() -> T,
        block: suspend (MpContext) -> Unit = {}
    ): T {
        logger.log(
            msg = "Request got, query = {}",
            level = Level.INFO,
            data = toLog("$logId-request-got")
        )
        block(this)
        return mapper().also {
            logger.log(
                msg = "Response ready, response = {}",
                level = Level.INFO,
                data = toLog("$logId-request-handled")
            )
        }
    }
}

