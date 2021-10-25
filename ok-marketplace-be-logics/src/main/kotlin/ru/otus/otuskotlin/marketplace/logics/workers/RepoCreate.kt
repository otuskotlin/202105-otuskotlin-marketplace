package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdModelRequest
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

internal fun ICorChainDsl<MpContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Data from request are stored in the DB Repository"

    on { status == CorStatus.RUNNING }

    handle {
        val result = adRepo.create(DbAdModelRequest(ad = dbAd))
        val resultValue = result.result
        if (result.isSuccess && resultValue != null) {
            responseAd =  resultValue
        } else {
            result.errors.forEach {
                addError(it)
            }
        }
    }
}
