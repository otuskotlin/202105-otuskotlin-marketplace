package ru.otus.otuskotlin.marketplace.services

import marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.IError

interface AdServiceInterface {

    fun createAd(mpContext: MpContext): MpContext

    fun readAd(mpContext: MpContext): MpContext

    fun updateAd(context: MpContext): MpContext

    fun deleteAd(context: MpContext): MpContext

    fun findAd(context: MpContext): MpContext
}