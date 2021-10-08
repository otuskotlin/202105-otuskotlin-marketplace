package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdModelRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import java.util.*
import kotlin.test.assertEquals


abstract class RepoAdUpdateTest {
    abstract val repo: IRepoAd

    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.update(DbAdModelRequest(updateObj)) }
        assertEquals(true, result.isSuccess)
        assertEquals(updateObj, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.update(DbAdModelRequest(updateObjNotFound)) }
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(listOf(CommonErrorModel(field = "id", message = "Not Found")), result.errors)
    }

    companion object: BaseInitAds() {
        override val initObjects: List<AdModel> = listOf(
            createInitTestModel("update")
        )
        private val updateId = initObjects.first().id
        private val updateIdNotFound = AdIdModel(UUID.randomUUID())

        private val updateObj = AdModel(
            id = updateId,
            title = "update object",
            description = "update object description",
            ownerId = OwnerIdModel(UUID.randomUUID()),
            visibility = AdVisibilityModel.REGISTERED_ONLY,
            dealSide = DealSideModel.PROPOSAL,
        )

        private val updateObjNotFound = AdModel(
            id = updateIdNotFound,
            title = "update object not found",
            description = "update object not found description",
            ownerId = OwnerIdModel(UUID.randomUUID()),
            visibility = AdVisibilityModel.REGISTERED_ONLY,
            dealSide = DealSideModel.PROPOSAL,
        )
    }
}
