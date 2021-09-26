package ru.otus.otuskotlin.marketplace.backend.repo.common

interface IRepoAd {
    suspend fun create(rq: DbAdModelRequest): DbAdResponse
    suspend fun read(rq: DbAdIdRequest): DbAdResponse
    suspend fun update(rq: DbAdModelRequest): DbAdResponse
    suspend fun delete(rq: DbAdIdRequest): DbAdResponse
    suspend fun search(rq: DbAdFilterRequest): DbAdsResponse

    object NONE : IRepoAd {
        override suspend fun create(rq: DbAdModelRequest): DbAdResponse {
            TODO("Not yet implemented")
        }

        override suspend fun read(rq: DbAdIdRequest): DbAdResponse {
            TODO("Not yet implemented")
        }

        override suspend fun update(rq: DbAdModelRequest): DbAdResponse {
            TODO("Not yet implemented")
        }

        override suspend fun delete(rq: DbAdIdRequest): DbAdResponse {
            TODO("Not yet implemented")
        }

        override suspend fun search(rq: DbAdFilterRequest): DbAdsResponse {
            TODO("Not yet implemented")
        }

    }
}
