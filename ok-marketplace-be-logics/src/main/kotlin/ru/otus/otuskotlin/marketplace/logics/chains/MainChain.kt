package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adCreateStub
import ru.otus.otuskotlin.marketplace.logics.workers.*
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object MainChain : ICorExec<MpContext> by chain<MpContext>({
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")

    chain {
        on { operation == MpContext.MpOperations.CREATE }
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
        worker {
            title = "Инициализация dbAd"
            on { status == CorStatus.RUNNING }
            handle {
                dbAd.ownerId = principal.id
                dbAd.dealSide = requestAd.dealSide
            }
        }
        accessValidation("Вычисление прав доступа")
        prepareAdForSaving("Подготовка объекта для сохранения")
        repoCreate("Запись объекта в БД")
        frontPermissions(title = "Вычисление пользовательских разрешений для фронтенда")

    }

    answerPrepareChain(title = "Подготовка ответа")

}).build()
