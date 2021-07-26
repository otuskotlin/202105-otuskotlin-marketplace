package ru.otus.otuskotlin.marketplace.backend.common.context

import ru.otus.otuskotlin.marketplace.backend.common.models.AdIdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.IError
import ru.otus.otuskotlin.marketplace.backend.common.models.PaginatedModel

data class MpContext(
    var onRequest: String = "",
    var requestAdId: AdIdModel = AdIdModel.NONE,
    var requestAd: AdModel = AdModel.NONE,
    var responseAd: AdModel = AdModel.NONE,
    var requestPage: PaginatedModel = PaginatedModel.NONE,
    var responsePage: PaginatedModel = PaginatedModel.NONE,
    var responseAds: MutableList<AdModel> = mutableListOf(),
    var errors: MutableList<IError> = mutableListOf(),
    var status: CorStatus = CorStatus.STARTED,
)
