package ru.otus.otuskotlin.markeplace.springapp.service

import org.springframework.stereotype.Service
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.openapi.models.AdProductBolt

@Service
class AdService {

    fun createAd(mpContext: MpContext): MpContext {
        return mpContext.apply {
            responseAd = mpContext.requestAd.apply {
                id = AdIdModel("34567")
                permissions = mutableSetOf(PermissionModel.READ)
            }
        }
    }

    fun getAd(mpContext: MpContext): MpContext {
        val requestedAdId = mpContext.requestAdId

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

    val ads = listOf(
        AdModel(
            id = AdIdModel(id = "98765"),
            title = "Крутой БОЛТ",
            description = "Лучше болта не найдете",
            ownerId = OwnerIdModel(id = "98765"),
            visibility = AdVisibilityModel.PUBLIC,
            dealSide = DealSideModel.DEMAND,
            permissions = mutableSetOf(PermissionModel.READ)
        )
    )


    val products = listOf(
        AdProductBolt(productType = null, lengh = null, diameter = null, threadPitch = null)
    )
}