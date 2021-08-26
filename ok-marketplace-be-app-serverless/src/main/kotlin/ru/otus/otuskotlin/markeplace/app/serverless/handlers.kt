package ru.otus.otuskotlin.markeplace.app.serverless

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.setQuery
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toCreateResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toDeleteResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toOffersResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toReadResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toSearchResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toUpdateResponse
import ru.otus.otuskotlin.marketplace.openapi.models.*
import java.time.Instant

class CreateAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, slContext: Context): APIGatewayV2HTTPResponse {
        val request = input.toTransportModel<CreateAdRequest>()
        val context = MpContext(
            startTime = Instant.now()
        )
        val result = try {
            runBlocking {
                adService.createAd(context, request)
            }
        } catch (e: Throwable) {
            runBlocking { adService.errorAd(context, e) as CreateAdResponse }
        }
        return result.toAPIGatewayV2HTTPResponse()
    }
}

class ReadAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, slContext: Context): APIGatewayV2HTTPResponse {
        val request = input.toTransportModel<ReadAdRequest>()
        val context = MpContext(
            startTime = Instant.now()
        )
        val result = try {
            runBlocking {
                adService.readAd(context, request)
            }
        } catch (e: Throwable) {
            runBlocking { adService.errorAd(context, e) as ReadAdResponse }
        }
        return result.toAPIGatewayV2HTTPResponse()
    }
}

class UpdateAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, slContext: Context): APIGatewayV2HTTPResponse {
        val request = input.toTransportModel<UpdateAdRequest>()
        val context = MpContext(
            startTime = Instant.now()
        )
        val result = try {
            runBlocking {
                adService.updateAd(context, request)
            }
        } catch (e: Throwable) {
            runBlocking { adService.errorAd(context, e) as UpdateAdResponse }
        }
        return result.toAPIGatewayV2HTTPResponse()
    }
}

class DeleteAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, slContext: Context): APIGatewayV2HTTPResponse {
        val request = input.toTransportModel<DeleteAdRequest>()
        val context = MpContext(
            startTime = Instant.now()
        )
        val result = try {
            runBlocking {
                adService.deleteAd(context, request)
            }
        } catch (e: Throwable) {
            runBlocking { adService.errorAd(context, e) as DeleteAdResponse }
        }
        return result.toAPIGatewayV2HTTPResponse()
    }
}

class SearchAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, slContext: Context): APIGatewayV2HTTPResponse {
        val request = input.toTransportModel<SearchAdRequest>()
        val context = MpContext(
            startTime = Instant.now()
        )
        val result = try {
            runBlocking {
                adService.searchAd(context, request)
            }
        } catch (e: Throwable) {
            runBlocking { adService.errorAd(context, e) as SearchAdResponse }
        }
        return result.toAPIGatewayV2HTTPResponse()
    }
}

class OffersAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, slContext: Context): APIGatewayV2HTTPResponse {
        val request = input.toTransportModel<OffersAdRequest>()
        val context = MpContext(
            startTime = Instant.now()
        )
        val result = try {
            runBlocking {
                adService.offersAd(context, request)
            }
        } catch (e: Throwable) {
            runBlocking { adService.errorAd(context, e) as OffersAdResponse }
        }
        return result.toAPIGatewayV2HTTPResponse()
    }
}

class InitAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, slContext: Context): APIGatewayV2HTTPResponse {
        val request = input.toTransportModel<InitAdRequest>()
        val context = MpContext(
            startTime = Instant.now()
        )
        val result = try {
            runBlocking {
                adService.initAd(context, request)
            }
        } catch (e: Throwable) {
            runBlocking { adService.errorAd(context, e) as InitAdResponse }
        }
        return result.toAPIGatewayV2HTTPResponse()
    }
}
