package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext

interface IRepoAd {
    suspend fun create(ctx: MpContext)
    suspend fun read(ctx: MpContext)
    suspend fun update(ctx: MpContext)
    suspend fun delete(ctx: MpContext)
    suspend fun search(ctx: MpContext)
}
