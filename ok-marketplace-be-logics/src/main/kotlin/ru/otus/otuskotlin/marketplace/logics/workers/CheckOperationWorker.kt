package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.exceptions.MpIllegalOperation
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

internal fun ICorChainDsl<MpContext>.checkOperationWorker(
    targetOperation: MpContext.MpOperations,
    title: String
) = worker{
    this.title = title
    description = "Если в контексте недопустимая операция, то чейн неуспешен"
    on {
        operation != targetOperation
    }
    handle {
        status = CorStatus.FAILING
        addError(
            e = MpIllegalOperation("Expected ${targetOperation.name} but was ${operation.name}")
        )
    }
}
