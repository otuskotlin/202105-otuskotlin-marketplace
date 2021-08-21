package ru.otus.otuskotlin.markeplace.app.serverless

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.setQuery
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toCreateResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toDeleteResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toOffersResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toReadResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toSearchResponse
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.toUpdateResponse
import ru.otus.otuskotlin.marketplace.openapi.models.CreateAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.DeleteAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.OffersAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.ReadAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.SearchAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.UpdateAdRequest

class CreateAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse =
        MpContext()
            .setQuery(input.toTransportModel<CreateAdRequest>())
            .let { adService.createAd(it) }
            .toCreateResponse()
            .toAPIGatewayV2HTTPResponse()
}

class ReadAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse =
        MpContext()
            .setQuery(input.toTransportModel<ReadAdRequest>())
            .let { adService.readAd(it) }
            .toReadResponse()
            .toAPIGatewayV2HTTPResponse()
}

class UpdateAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse =
        MpContext()
            .setQuery(input.toTransportModel<UpdateAdRequest>())
            .let { adService.updateAd(it) }
            .toUpdateResponse()
            .toAPIGatewayV2HTTPResponse()
}

class DeleteAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse =
        MpContext()
            .setQuery(input.toTransportModel<DeleteAdRequest>())
            .let { adService.deleteAd(it) }
            .toDeleteResponse()
            .toAPIGatewayV2HTTPResponse()
}

class SearchAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse =
        MpContext()
            .setQuery(input.toTransportModel<SearchAdRequest>())
            .let { adService.searchAd(it) }
            .toSearchResponse()
            .toAPIGatewayV2HTTPResponse()
}

class OffersAdHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse =
        MpContext()
            .setQuery(input.toTransportModel<OffersAdRequest>())
            .let { adService.offersAd(it) }
            .toOffersResponse()
            .toAPIGatewayV2HTTPResponse()
}
