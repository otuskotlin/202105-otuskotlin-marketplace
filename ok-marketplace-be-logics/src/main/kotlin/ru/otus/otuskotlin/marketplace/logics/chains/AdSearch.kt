package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.MpSearchTypes
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserPermissions
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adSearchStub
import ru.otus.otuskotlin.marketplace.logics.workers.*

object AdSearch : ICorExec<MpContext> by chain<MpContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = MpContext.MpOperations.SEARCH,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
    adSearchStub(title = "Обработка стабкейса для SEARCH")

    mpValidation {
    }

    chainPermissions("Вычисление разрешений для пользователя")
    chain {
        title = "Подготовка поискового запроса"
        description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
        on { status == CorStatus.RUNNING }
        worker {
            title = "Определение типа поиска"
            description = title
            handle {
                dbFilter.searchTypes = listOf(
                    MpSearchTypes.OWN.takeIf { chainPermissions.contains(MpUserPermissions.SEARCH_OWN) },
                    MpSearchTypes.PUBLIC.takeIf { chainPermissions.contains(MpUserPermissions.SEARCH_PUBLIC) },
                    MpSearchTypes.REGISTERED.takeIf { chainPermissions.contains(MpUserPermissions.SEARCH_REGISTERED) },
                ).filterNotNull().toMutableSet()
            }
        }
        worker("Копируем все поля бизнес-поиска") {
            dbFilter.dealSide = requestFilter.dealSide
            dbFilter.ownerId = requestFilter.ownerId
            dbFilter.searchStr = requestFilter.searchStr
        }
    }

    repoSearch(title = "Поиск объявлений в БД")

    answerPrepareChain(title = "Подготовка ответа")
}).build()
