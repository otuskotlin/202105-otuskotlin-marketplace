package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorExecDsl
import ru.otus.otuskotlin.marketplace.common.cor.worker

object ChainInitWorker: ICorExecDsl<MpContext> by worker({
        title = "Инициализация статуса"
        description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

        on {
            status == CorStatus.NONE
        }
        handle {
            status = CorStatus.RUNNING
        }
})
