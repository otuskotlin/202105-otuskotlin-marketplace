package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import java.util.*
import kotlin.test.assertEquals


abstract class RepoAdSearchTest {
    abstract val repo: IRepoAd

    @Test
    fun searchOwner() {
        val result = runBlocking { repo.search(DbAdFilterRequest(ownerId = searchOwnerId)) }
        assertEquals(true, result.isSuccess)
        val expected = listOf(initObjects[1], initObjects[3])
        assertEquals(expected.sortedBy { it.id.asString() }, result.result.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchDealSide() {
        val result = runBlocking { repo.search(DbAdFilterRequest(dealSide = DealSideModel.PROPOSAL)) }
        assertEquals(true, result.isSuccess)
        val expected = listOf(initObjects[2], initObjects[4])
        assertEquals(expected.sortedBy { it.id.asString() }, result.result.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitAds() {

        val searchOwnerId = OwnerIdModel(UUID.randomUUID())
        override val initObjects: List<AdModel> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3", dealSide = DealSideModel.PROPOSAL),
            createInitTestModel("ad4", ownerId = searchOwnerId),
            createInitTestModel("ad5", dealSide = DealSideModel.PROPOSAL),
        )
    }
}
