package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorExecDsl
import ru.otus.otuskotlin.marketplace.common.cor.worker

class CheckOperationWorker(targetOperation: MpContext.MpOperations): ICorExecDsl<MpContext> by worker({
    title = "Проверка, что операция соответствует выбранному чейну"
    on { operation != targetOperation }
    handle {
        status = CorStatus.FAILING
    }
})
