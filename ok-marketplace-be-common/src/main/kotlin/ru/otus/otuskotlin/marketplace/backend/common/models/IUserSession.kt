package ru.otus.otuskotlin.marketplace.backend.common.models

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext

interface IUserSession<T> {
    val fwSession: T

    suspend fun notifyAdChanged(context: MpContext)

    companion object {
        object EmptySession : IUserSession<Unit> {
            override val fwSession: Unit = Unit
            override suspend fun notifyAdChanged(context: MpContext) {
                TODO("Not yet implemented")
            }

        }
    }
}

