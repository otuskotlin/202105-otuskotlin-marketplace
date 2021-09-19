package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adOfferStub
import ru.otus.otuskotlin.marketplace.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.marketplace.logics.workers.chainInitWorker
import ru.otus.otuskotlin.marketplace.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object AdOffer: ICorExec<MpContext> by chain<MpContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = MpContext.MpOperations.OFFER,
    )
    chainInitWorker(title = "Инициализация чейна")
    adOfferStub(title = "Обработка стабкейса для OFFER")

    mpValidation {
        validate<String?> {
            on {
                requestAdId.asString() }
            validator(ValidatorStringNonEmpty(field = "id"))
        }
    }

    // TODO: продовая логика, работа с БД

    answerPrepareChain(title = "Подготовка ответа")
}).build()
