package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

internal fun ICorChainDsl<MpContext>.answerPrepareChain(title: String) = chain{
    this.title = title
    description = "Чейн считается успешным, если в нем не было ошибок и он отработал"
    worker {
        this.title = "Обработчик успешного чейна"
        on { status in setOf(CorStatus.RUNNING, CorStatus.FINISHING) }
        handle {
            status = CorStatus.SUCCESS
        }
    }
    worker {
        this.title = "Обработчик неуспешного чейна"
        on { status != CorStatus.SUCCESS }
        handle {
            status = CorStatus.ERROR
            responseAd = AdModel()
            responseAds = mutableListOf()
        }
    }
}
