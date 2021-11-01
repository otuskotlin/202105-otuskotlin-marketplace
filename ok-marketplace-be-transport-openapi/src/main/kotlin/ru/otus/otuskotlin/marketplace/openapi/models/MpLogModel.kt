package ru.otus.otuskotlin.marketplace.openapi.models

data class MpLogModel(
    val requestAdId: String? = null,
    val requestAd: ResponseAd? = null,
    val responseAd: ResponseAd? = null,
    val responseAds: List<ResponseAd>? = null,
)
