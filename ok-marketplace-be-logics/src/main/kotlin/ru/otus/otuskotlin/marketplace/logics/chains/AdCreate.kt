package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adCreateStub
import ru.otus.otuskotlin.marketplace.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.marketplace.logics.workers.chainInitWorker
import ru.otus.otuskotlin.marketplace.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object AdCreate: ICorExec<MpContext> by chain<MpContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = MpContext.MpOperations.CREATE,
    )
    chainInitWorker(title = "Инициализация чейна")
    adCreateStub(title = "Обработка стабкейса для CREATE")

    mpValidation {
        validate<String?> {
            on { requestAd.title }
            validator(ValidatorStringNonEmpty(field = "title"))
        }
        validate<String?> {
            on { requestAd.description }
            validator(ValidatorStringNonEmpty(field = "description"))
        }
    }


    // TODO: продовая логика, работа с БД

    answerPrepareChain(title = "Подготовка ответа")
}).build()
