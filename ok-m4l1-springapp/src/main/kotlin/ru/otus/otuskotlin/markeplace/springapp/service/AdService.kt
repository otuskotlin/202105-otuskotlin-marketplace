package ru.otus.otuskotlin.markeplace.springapp.service

import org.springframework.stereotype.Service
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.*

@Service
class AdService {

    fun createAd(mpContext: MpContext): MpContext {
        return mpContext.apply {
            responseAd = mpContext.requestAd.also {
                it.apply { id = AdIdModel(it.ownerId.id) }
                ads.add(it)
            }
        }
    }

    fun getAd(mpContext: MpContext): MpContext {
        val requestedAdId = mpContext.requestAdId

        if (requestedAdId.id.isEmpty()) {
            return mpContext.apply {
                responseAds
            }
        }

        val foundedAd = ads.find { it.id == requestedAdId }

        return foundedAd?.let {
            mpContext.apply {
                responseAd = it
            }
        } ?: mpContext.apply {
            errors = mutableListOf(
                CommonErrorModel(
                    field = "requestedAdId",
                    level = IError.Level.ERROR,
                    message = "Not found ad by id ${requestedAdId.id}"
                )
            )
            status = CorStatus.FAILING
        }
    }

    fun updateAd(context: MpContext): MpContext {
        val requestedAd = context.requestAd

        val newAd = ads.find { it.id.id == requestedAd.id.id }
            ?.update(requestedAd)
            ?: requestedAd.also { ads.add(requestedAd) }

        return context.apply {
            responseAd = newAd
        }
    }

    fun deleteAd(context: MpContext): MpContext {
        val adIdForDeletion = context.requestAdId

        val indexOfAd = ads.indexOfFirst { it.id == adIdForDeletion }
        return if (indexOfAd == -1) {
            context.apply {
                status = CorStatus.FAILING
                errors.add(
                    CommonErrorModel(
                        field = "id",
                        level = IError.Level.WARNING,
                        message = "Ad with id ${adIdForDeletion.id} doesn't exist"
                    )
                )
            }
        } else {
            context.apply {
                responseAd = ads.removeAt(indexOfAd)
            }
        }
    }

    fun findAd(context: MpContext): MpContext {
        val requestPage = context.requestPage

        val idToFind = requestPage.lastId

        return ads.find { it.id == idToFind }
            ?.let {
                context.apply {
                    responseAds.add(it)
                }
            }
            ?: context.apply {
                status = CorStatus.FAILING
                errors.add(
                    CommonErrorModel(
                        field = "id",
                        level = IError.Level.ERROR,
                        message = "Ad with id ${idToFind.id} doesn't exist"
                    )
                )
            }
    }

    fun getOffers(context: MpContext): MpContext {
        val requestedId = context.requestAdId
        val lastAdModelIdOnPage = context.requestPage.lastId

        if (requestedId.id.isEmpty()) {
            return context.apply {
                responseAds = ads
            }
        }

        return ads.filterTo(mutableListOf()) { it.id == requestedId || it.id == lastAdModelIdOnPage }
            .let { foundAds ->
                context.apply {
                    responseAds.addAll(foundAds)
                    responsePage = requestPage
                }
            }
    }

    val ads = mutableListOf(
        AdModel(
            id = AdIdModel(id = "98765"),
            title = "Средний БОЛТ",
            description = "Можно найти болт получше",
            ownerId = OwnerIdModel(id = "98765"),
            visibility = AdVisibilityModel.PUBLIC,
            dealSide = DealSideModel.DEMAND,
            permissions = mutableSetOf(PermissionModel.READ)
        )
    )
}

private fun AdModel.update(updateableAd: AdModel) = apply {
    title = updateableAd.title
    description = updateableAd.description
    ownerId = updateableAd.ownerId
    visibility = updateableAd.visibility
    dealSide = updateableAd.dealSide
    permissions.addAll(updateableAd.permissions)
}