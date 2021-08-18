package ru.otus.otuskotlin.markeplace.app.serverless

import marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel

class AdService {
    fun createAd(context: MpContext): MpContext =
        context.apply {
            responseAd = requestAd
        }

    fun readAd(context: MpContext): MpContext =
        context.apply {
            if (Bolt.isCorrectId(requestAdId.id)) {
                responseAd = Bolt.getModel()
            } else {
                status = CorStatus.FAILING
                errors.add(CommonErrorModel(field = "requestAdId", message = "No ad found"))
            }
        }

    fun updateAd(context: MpContext): MpContext =
        context.apply {
            if  (Bolt.isCorrectId(requestAd.id.id)) {
                responseAd = requestAd
            } else {
                status = CorStatus.FAILING
                errors.add(CommonErrorModel(field = "requestAd.id", message = "No ad found"))
            }
        }

    fun deleteAd(context: MpContext): MpContext =
        context.apply {
            if  (Bolt.isCorrectId(requestAdId.id)) {
                responseAd = Bolt.getModel()
            } else {
                status = CorStatus.FAILING
                errors.add(CommonErrorModel(field = "requestAdId", message = "No ad found"))
            }
        }

    fun searchAd(context: MpContext): MpContext =
        context.apply {
            responseAds = Bolt.getModels().toMutableList()
        }

    fun offersAd(context: MpContext): MpContext =
        context.apply {
            responseAds = Bolt.getModels().toMutableList()
        }
}

val adService = AdService()