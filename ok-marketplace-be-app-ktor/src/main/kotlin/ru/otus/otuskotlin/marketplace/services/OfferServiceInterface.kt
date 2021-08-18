package ru.otus.otuskotlin.marketplace.services

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext

interface OfferServiceInterface {
    fun readOffers(context: MpContext): MpContext
}