package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

internal fun ICorChainDsl<MpContext>.chainInitWorker(title: String) = worker {
        this.title = title
        description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

        on {
            status == CorStatus.NONE
        }
        handle {
            status = CorStatus.RUNNING
        }
}
