package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.AccessTableConditions
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.accessTable
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.resolveRelationsTo

fun ICorChainDsl<MpContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { status == CorStatus.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        dbAd.principalRelations = dbAd.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = dbAd.principalRelations.flatMap { relation ->
            chainPermissions.map { permission ->
                AccessTableConditions(
                    operation = operation,
                    permission = permission,
                    relation = relation,
                )
            }
        }
            .any {
                accessTable[it] ?: false
            }
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            addError(
                CommonErrorModel(message = "User is not allowed to this operation")
            )
        }
    }
}
