package ru.otus.otuskotlin.marketplace.backend.repo.common

interface IRepoAd {
    suspend fun create(req: DbAdModelRequest): DbAdResponse
    suspend fun read(req: DbAdIdRequest): DbAdResponse
    suspend fun update(req: DbAdModelRequest): DbAdResponse
    suspend fun delete(req: DbAdIdRequest): DbAdResponse
    suspend fun search(req: DbAdFilterRequest): DbAdsResponse

    object NONE : IRepoAd {
        override suspend fun create(req: DbAdModelRequest): DbAdResponse {
            TODO("Not yet implemented")
        }

        override suspend fun read(req: DbAdIdRequest): DbAdResponse {
            TODO("Not yet implemented")
        }

        override suspend fun update(req: DbAdModelRequest): DbAdResponse {
            TODO("Not yet implemented")
        }

        override suspend fun delete(req: DbAdIdRequest): DbAdResponse {
            TODO("Not yet implemented")
        }

        override suspend fun search(req: DbAdFilterRequest): DbAdsResponse {
            TODO("Not yet implemented")
        }

    }
}
