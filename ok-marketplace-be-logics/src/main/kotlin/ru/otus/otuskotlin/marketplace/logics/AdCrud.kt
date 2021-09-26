package ru.otus.otuskotlin.marketplace.logics

import ru.otus.otuskotlin.marketplace.backend.common.context.ContextConfig
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.logics.chains.*

/**
 * Класс-фасад, содержащий все методы бизнес-логики
 */
class AdCrud(val config: ContextConfig = ContextConfig()) {
    suspend fun create(context: MpContext) {
        AdCreate.exec(context.initSettings())
    }
    suspend fun read(context: MpContext) {
        AdRead.exec(context.initSettings())
    }
    suspend fun update(context: MpContext) {
        AdUpdate.exec(context.initSettings())
    }
    suspend fun delete(context: MpContext) {
        AdDelete.exec(context.initSettings())
    }
    suspend fun search(context: MpContext) {
        AdSearch.exec(context.initSettings())
    }
    suspend fun offer(context: MpContext) {
        AdOffer.exec(context.initSettings())
    }

    // Метод для установки параметров чейна в контекст, параметры передаются в конструкторе класса
    private fun MpContext.initSettings() = apply {
        config = this@AdCrud.config
    }
}
