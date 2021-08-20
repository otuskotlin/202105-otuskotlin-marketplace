package ru.otus.otuskotlin.marketplace.services

import marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.IError

class AdService {
    fun createAd(mpContext: MpContext): MpContext {
        return mpContext.apply {
            responseAd = Bolt.getModel()
        }
    }

    fun readAd(mpContext: MpContext): MpContext {
        val requestedId = mpContext.requestAdId.id
        val shouldReturnStub = Bolt.isCorrectId(requestedId)

        return if (shouldReturnStub) {
            mpContext.apply {
                responseAd = Bolt.getModel()
            }
        } else {
            mpContext.addError {
                field = "requestedAdId"
                message = "Not found ad by id $requestedId"
            }
        }
    }

    fun updateAd(context: MpContext) = context.apply {
        responseAd = requestAd
    }


    fun deleteAd(context: MpContext): MpContext {
        val shouldReturnStub = Bolt.isCorrectId(context.requestAdId.id)

        return if (shouldReturnStub) {
            context.apply {
                responseAd = requestAd
            }
        } else {
            context.addError {
                field = "id"
                level = IError.Level.WARNING
                message = "Ad with id ${context.requestAd.id.id} doesn't exist"
            }
        }
    }

    fun findAd(context: MpContext): MpContext {
        val requestPage = context.requestPage

        val idToFind = requestPage.lastId

        val shouldReturnStub = Bolt.isCorrectId(idToFind.id)

        return if (shouldReturnStub) {
            context.apply {
                responseAds.addAll(
                    Bolt.getModels()
                )
            }
        } else {
            context.addError {
                field = "id"
                level = IError.Level.WARNING
                message = "Ad with id ${idToFind.id} doesn't exist"
            }
        }
    }
}

