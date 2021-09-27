package ru.otus.otuskotlin.marketplace.backend.common.context

import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd

data class ContextConfig(
    val repoProd: IRepoAd = IRepoAd.NONE,
    val repoTest: IRepoAd = IRepoAd.NONE,
)
