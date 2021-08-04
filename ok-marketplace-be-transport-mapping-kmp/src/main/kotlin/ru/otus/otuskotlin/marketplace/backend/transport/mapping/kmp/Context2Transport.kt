package ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.kmp.transport.models.*

fun MpContext.toInitResponse() = InitAdResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    result = if (errors.find { it.level == IError.Level.ERROR } == null) BaseResponse.Result.SUCCESS
                else BaseResponse.Result.ERROR
)

fun MpContext.toReadResponse() = ReadAdResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    readAd = responseAd.takeIf { it != AdModel() }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    result = if (errors.find { it.level == IError.Level.ERROR } == null) BaseResponse.Result.SUCCESS
                else BaseResponse.Result.ERROR
)

fun MpContext.toCreateResponse() = CreateAdResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    createdAd = responseAd.takeIf { it != AdModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) BaseResponse.Result.SUCCESS
                else BaseResponse.Result.ERROR
)

fun MpContext.toUpdateResponse() = UpdateAdResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    updatedAd = responseAd.takeIf { it != AdModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) BaseResponse.Result.SUCCESS
                else BaseResponse.Result.ERROR
)

fun MpContext.toDeleteResponse() = DeleteAdResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    deletedAd = responseAd.takeIf { it != AdModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) BaseResponse.Result.SUCCESS
                else BaseResponse.Result.ERROR
)

fun MpContext.toOffersResponse() = OffersAdResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    offeredAds = responseAds.takeIf { it.isNotEmpty() }?.filter { it != AdModel() }?.map { it.toTransport() },
    page = responsePage.takeIf { it != PaginatedModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) BaseResponse.Result.SUCCESS
                else BaseResponse.Result.ERROR
)

fun MpContext.toSearchResponse() = SearchAdResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    foundAds = responseAds.takeIf { it.isNotEmpty() }?.filter { it != AdModel() }?.map { it.toTransport() },
    page = responsePage.takeIf { it != PaginatedModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) BaseResponse.Result.SUCCESS
                else BaseResponse.Result.ERROR
)

private fun PaginatedModel.toTransport() = BasePaginatedResponse(
    size = size.takeIf { it != Int.MIN_VALUE },
    lastId = lastId.takeIf { it != AdIdModel.NONE }?.asString(),
    position = position.takeIf { it != PaginatedModel.PositionModel.NONE }
        ?.let { BasePaginatedResponse.Position.valueOf(it.name) }
)

private fun IError.toTransport() = RequestError(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
)

private fun AdModel.toTransport() = ResponseAd(
    id = id.takeIf { it != AdIdModel.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != OwnerIdModel.NONE }?.asString(),
    visibility = visibility.takeIf { it != AdVisibilityModel.NONE }?.let { AdVisibility.valueOf(it.name) },
    dealSide = dealSide.takeIf { it != DealSideModel.NONE }?.let { AdDealSide.valueOf(it.name) },
    permissions = permissions.takeIf { it.isNotEmpty() }?.filter { it != PermissionModel.NONE }
        ?.map { AdPermissions.valueOf(it.name) }?.toSet(),
)
