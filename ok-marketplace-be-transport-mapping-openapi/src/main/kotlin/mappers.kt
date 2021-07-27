package ru.otus.otuskotlin.marketplace.backend.transport.mappers

import ru.otus.otuskotlin.marketpalce.openapi.models.AdDealSide
import ru.otus.otuskotlin.marketpalce.openapi.models.AdVisibility
import ru.otus.otuskotlin.marketpalce.openapi.models.CreateAdRequest
import ru.otus.otuskotlin.marketpalce.openapi.models.CreateableAd
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.*

fun MpContext.setQuery(query: CreateAdRequest) {
    requestAd = query.createAd?.toInner() ?: AdModel()
}

private fun CreateableAd.toInner(): AdModel = AdModel(
    id = AdIdModel.NONE,
    title = title ?: "",
    description = description ?: "",
    ownerId = ownerId?.let { OwnerIdModel(it) } ?: OwnerIdModel.NONE,
    visibility = visibility?.toModel() ?: AdVisibilityModel.NONE,
    dealSide = dealSide?.toModel() ?: DealSideModel.NONE,
)

private fun AdDealSide.toModel(): DealSideModel = when(this) {
    AdDealSide.DEMAND -> DealSideModel.DEMAND
    AdDealSide.PROPOSAL -> DealSideModel.PROPOSAL
}

private fun AdVisibility.toModel(): AdVisibilityModel = when(this) {
    AdVisibility.REGISTERED_ONLY -> AdVisibilityModel.REGISTERED_ONLY
    AdVisibility.OWNER_ONLY -> AdVisibilityModel.OWNER_ONLY
    AdVisibility.PUBLIC -> AdVisibilityModel.PUBLIC
}
