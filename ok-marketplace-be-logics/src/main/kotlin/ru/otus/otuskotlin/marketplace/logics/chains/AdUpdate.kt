package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adUpdateStub
import ru.otus.otuskotlin.marketplace.logics.workers.*
import ru.otus.otuskotlin.marketplace.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.marketplace.logics.workers.chainInitWorker
import ru.otus.otuskotlin.marketplace.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.marketplace.logics.workers.chooseDb
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object AdUpdate: ICorExec<MpContext> by chain<MpContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = MpContext.MpOperations.UPDATE,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
    adUpdateStub(title = "Обработка стабкейса для UPDATE")

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

    repoUpdate(title = "Обновление данных об объекте в БД")

    answerPrepareChain(title = "Подготовка ответа")
}).build()
