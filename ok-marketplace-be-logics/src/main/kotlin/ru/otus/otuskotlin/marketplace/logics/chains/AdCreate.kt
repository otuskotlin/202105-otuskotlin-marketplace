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
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adCreateStub
import ru.otus.otuskotlin.marketplace.logics.workers.*
import ru.otus.otuskotlin.marketplace.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.marketplace.logics.workers.chainInitWorker
import ru.otus.otuskotlin.marketplace.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.marketplace.logics.workers.chooseDb
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object AdCreate: ICorExec<MpContext> by chain<MpContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = MpContext.MpOperations.CREATE,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
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

    chainPermissions("Вычисление разрешений для пользователя")

    chain {
        on { status == CorStatus.RUNNING }
        worker() {
            title = "Валидация прав доступа"
            description = "Проверка наличия прав для записи объектов в БД"
            on { MpUserPermissions.CREATE_OWN !in chainPermissions }
            handle {
                addError(
                    CommonErrorModel(message = "User is not allowed to create Ads")
                )
            }
        }
    }

    worker {
        title = "Подготовка объкта к записи"
        description = "Явное выставление параметров объекта для сохранения в базу данных"
        on { status == CorStatus.RUNNING }
        handle {
            // Копируем в отдельное свойство для подготовки к сохранению.
            // Копирование для того, чтоб иметь возможность логировать иходные данные из запроса
            dbAd = requestAd.copy(
                ownerId = principal.id
            )
        }
    }


    repoCreate("Запись объекта в БД")

    frontPermissions(title = "Вычисление пользовательских разрешений для фронтенда")

    answerPrepareChain(title = "Подготовка ответа")
}).build()
