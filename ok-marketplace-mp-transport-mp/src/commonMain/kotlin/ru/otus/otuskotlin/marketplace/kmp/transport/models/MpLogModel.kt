package ru.otus.otuskotlin.marketplace.kmp.transport.models

data class MpLogModel(
    val requestAdId: String? = null,
    val requestAd: ResponseAd? = null,
    val responseAd: ResponseAd? = null,
    val responseAds: List<ResponseAd>? = null,
)
