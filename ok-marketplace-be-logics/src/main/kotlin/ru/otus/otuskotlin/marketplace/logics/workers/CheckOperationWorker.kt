package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.worker

internal fun checkOperation(targetOperation: MpContext.MpOperations) = worker<MpContext> {
    title = "Проверка, что операция соответствует выбранному чейну"
    on { operation != targetOperation }
    handle {
        status = CorStatus.FAILING
        println("Error operation")
    }
}
