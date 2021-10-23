package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserPermissions
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adDeleteStub
import ru.otus.otuskotlin.marketplace.logics.workers.*
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object AdDelete: ICorExec<MpContext> by chain<MpContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = MpContext.MpOperations.DELETE,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
    adDeleteStub(title = "Обработка стабкейса для DELETE")

    mpValidation {
        validate<String?> {
            on { requestAdId.asString() }
            validator(ValidatorStringNonEmpty(field = "id"))
        }
    }

    chainPermissions("Вычисление разрешений для пользователя")

    chain {
        on { status == CorStatus.RUNNING }
        worker() {
            title = "Валидация прав доступа"
            description = "Проверка наличия прав для удаления объектов из БД"
            on { MpUserPermissions.CREATE_OWN !in chainPermissions }
            handle {
                addError(
                    CommonErrorModel(message = "User is not allowed to create Ads")
                )
            }
        }
    }


    repoDelete(title = "Удаление объекта из БД")

    answerPrepareChain(title = "Подготовка ответа")
}).build()
