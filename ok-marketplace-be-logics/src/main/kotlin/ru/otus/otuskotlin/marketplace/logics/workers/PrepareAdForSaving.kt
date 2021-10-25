package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

fun ICorChainDsl<MpContext>.prepareAdForSaving(title: String) {
    worker {
        this.title = title
        description = title
        on { status == CorStatus.RUNNING }
        handle {
            with(dbAd) {
                this.title = requestAd.title
                description = requestAd.description
                visibility = requestAd.visibility
            }
        }
    }
}
