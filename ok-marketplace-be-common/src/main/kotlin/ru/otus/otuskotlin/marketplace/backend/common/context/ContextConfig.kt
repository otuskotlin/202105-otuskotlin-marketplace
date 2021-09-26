package ru.otus.otuskotlin.marketplace.backend.common.context

data class ContextConfig(
    val repoProd: IRepoAd = IRepoAd.NONE,
    val repoTest: IRepoAd = IRepoAd.NONE
)
