package ru.otus.otuskotlin.marketplace.backend.repo.common

interface IRepoAd {
    suspend fun create(rq: DbAdModelRequest): DbAdResponse
    suspend fun read(rq: DbAdIdRequest): DbAdResponse
    suspend fun update(rq: DbAdModelRequest): DbAdResponse
    suspend fun delete(rq: DbAdIdRequest): DbAdResponse
    suspend fun search(rq: DbAdFilterRequest): DbAdsResponse
}
