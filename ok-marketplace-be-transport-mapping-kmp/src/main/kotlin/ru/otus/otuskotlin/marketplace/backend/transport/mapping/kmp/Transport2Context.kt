package ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.kmp.transport.models.*

fun MpContext.setQuery(query: InitAdRequest) = apply {
    onRequest = query.requestId?:""
}

fun MpContext.setQuery(query: CreateAdRequest) = apply {
    onRequest = query.requestId?:""
    requestAd = query.createAd?.toModel()?: AdModel()
}

fun MpContext.setQuery(query: ReadAdRequest) = apply {
    onRequest = query.requestId?:""
    requestAdId = AdIdModel(query.readAdId?:"")
}

fun MpContext.setQuery(query: UpdateAdRequest) = apply {
    onRequest = query.requestId?:""
    requestAd = query.createAd?.toModel()?: AdModel()
}

fun MpContext.setQuery(query: DeleteAdRequest) = apply {
    onRequest = query.requestId?:""
    requestAdId = AdIdModel(query.deleteAdId?:"")
}

fun MpContext.setQuery(query: OffersAdRequest) = apply {
    onRequest = query.requestId?:""
    requestPage = query.page?.toModel()?: PaginatedModel()
    requestAdId = AdIdModel(query.deleteAdId?:"")
}

fun MpContext.setQuery(query: SearchAdRequest) = apply {
    onRequest = query.requestId?:""
    requestPage = query.page?.toModel()?: PaginatedModel()
}

private fun BasePaginatedRequest.toModel() = PaginatedModel(
    size = size?: Int.MIN_VALUE,
    lastId = AdIdModel(lastId?:""),
)

private fun UpdateableAd.toModel() = AdModel(
    id = AdIdModel(id?:""),
    title = title?:"",
    description = description?:"",
    ownerId = OwnerIdModel(ownerId?:""),
    visibility = visibility?.let { AdVisibilityModel.valueOf(it.name) }?:AdVisibilityModel.NONE,
    dealSide = dealSide?.let { DealSideModel.valueOf(it.name) }?:DealSideModel.NONE,
)

private fun CreateableAd.toModel() = AdModel(
    title = title?:"",
    description = description?:"",
    ownerId = OwnerIdModel(ownerId?:""),
    visibility = visibility?.let { AdVisibilityModel.valueOf(it.name) }?:AdVisibilityModel.NONE,
    dealSide = dealSide?.let { DealSideModel.valueOf(it.name) }?:DealSideModel.NONE,
)
