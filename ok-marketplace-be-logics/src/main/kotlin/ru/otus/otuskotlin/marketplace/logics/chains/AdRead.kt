package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adReadStub
import ru.otus.otuskotlin.marketplace.logics.workers.*
import ru.otus.otuskotlin.marketplace.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.marketplace.logics.workers.chainInitWorker
import ru.otus.otuskotlin.marketplace.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.marketplace.logics.workers.chooseDb
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object AdRead : ICorExec<MpContext> by chain<MpContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = MpContext.MpOperations.READ,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
    adReadStub(title = "Обработка стабкейса для READ")

    mpValidation {
        validate<String?> {
            on { requestAdId.asString() }
            validator(ValidatorStringNonEmpty(field = "id"))
        }
    }

    chainPermissions("Вычисление разрешений для пользователя")
    repoRead(title = "Чтение объекта из БД")
    accessValidation("Вычисление прав доступа")

    worker {
        title = "Подготовка результата к отправке"
        description = title
        on { status == CorStatus.RUNNING }
        handle { responseAd = dbAd }
    }

    frontPermissions(title = "Вычислений разрешений для фронтенда")

    answerPrepareChain(title = "Подготовка ответа")
}).build()

