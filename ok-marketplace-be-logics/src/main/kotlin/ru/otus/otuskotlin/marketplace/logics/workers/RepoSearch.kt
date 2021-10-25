package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.MpStubCase
import ru.otus.otuskotlin.marketplace.backend.common.models.WorkMode
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdModelRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

internal fun ICorChainDsl<MpContext>.repoSearch(title: String) = worker {
    this.title = "Search Ads"
    description = """
            Search for Ads those are most appropriate to the request criteria
        """.trimIndent()

    on { status == CorStatus.RUNNING }

    handle {
        val result = adRepo.search(DbAdFilterRequest.of(dbFilter))
        if (result.isSuccess) {
            responseAds = result.result.toMutableList()
        } else {
            result.errors.forEach {
                addError(it)
            }
        }
    }
}
