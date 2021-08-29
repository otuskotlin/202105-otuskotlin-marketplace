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

object AdSearchStub: ICorExecDsl<MpContext> by chain({
    title = "Обработка стабкейса для SEARCH"
    on { status == CorStatus.RUNNING && stubCase != MpStubCase.NONE }

    worker {
        title = "Успешный стабкейс для SEARCH"
        on { stubCase == MpStubCase.SUCCESS }
        handle {
            responseAds = Bolt.getModels().toMutableList()
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
