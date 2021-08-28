package ru.otus.otuskotlin.marketplace.logics

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext

/**
 * Класс-фасад, содержащий все методы бизнес-логики
 */
class AdCrud {
    suspend fun init(context: MpContext) {}
    suspend fun create(context: MpContext) {}
    suspend fun read(context: MpContext) {}
    suspend fun update(context: MpContext) {}
    suspend fun delete(context: MpContext) {}
    suspend fun search(context: MpContext) {}
    suspend fun offer(context: MpContext) {}
}
