package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adUpdateStub
import ru.otus.otuskotlin.marketplace.logics.workers.*
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object AdUpdate : ICorExec<MpContext> by chain<MpContext>({
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

    chainPermissions("Вычисление разрешений для пользователя")
    worker(title = "инициализируем requestAdId") { requestAdId = requestAd.id }
    repoRead(title = "Чтение объекта из БД")
    worker {
        title = "Инициализация dbAd"
        on { status == CorStatus.RUNNING && dbAd.dealSide != requestAd.dealSide }
        handle {
            addError(
                CommonErrorModel(field = "dealSide", message = "You are not allowed to change the deal side")
            )
        }
    }
    accessValidation("Вычисление прав доступа")
    prepareAdForSaving("Подготовка объекта для сохранения")
    repoUpdate(title = "Обновление данных об объекте в БД")

    answerPrepareChain(title = "Подготовка ответа")
}).build()

