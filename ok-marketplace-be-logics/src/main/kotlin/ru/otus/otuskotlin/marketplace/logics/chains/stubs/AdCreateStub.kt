package ru.otus.otuskotlin.marketplace.logics.chains.stubs

import marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.exceptions.MpStubCaseNotFound
import ru.otus.otuskotlin.marketplace.backend.common.models.IError
import ru.otus.otuskotlin.marketplace.backend.common.models.MpStubCase
import ru.otus.otuskotlin.marketplace.common.cor.ICorExecDsl
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

object AdCreateStub: ICorExecDsl<MpContext> by chain({
    title = "Обработка стабкейса для CREATE"
    on { status == CorStatus.RUNNING && stubCase != MpStubCase.NONE }

    worker {
        title = "Успешный стабкейс для CREATE"
        on { stubCase == MpStubCase.SUCCESS }
        handle {
            responseAd = requestAd.copy(id = Bolt.getModel().id)
            status = CorStatus.FINISHING
        }
    }

    worker {
        title = "Обработка отсутствия подходящего стабкейса"
        on { status == CorStatus.RUNNING }
        handle {
            status = CorStatus.FAILING
            addError(
                e = MpStubCaseNotFound(stubCase.name),
            )
        }
    }

})
