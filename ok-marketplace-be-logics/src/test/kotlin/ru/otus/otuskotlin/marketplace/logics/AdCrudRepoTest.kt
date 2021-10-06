package ru.otus.otuskotlin.marketplace.logics

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.backend.common.context.ContextConfig
import ru.otus.otuskotlin.marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
class AdCrudRepoTest {

    @Test
    fun createSuccessTest() {
        val repo = RepoAdInMemory()
        val crud = AdCrud(config = ContextConfig(
            repoTest = repo
        ))
        val context = MpContext(
            workMode = WorkMode.TEST,
            requestAd = Bolt.getModel { id = AdIdModel.NONE },
            operation = MpContext.MpOperations.CREATE,
        )
        runBlocking {
            crud.create(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = Bolt.getModel()
        with(context.responseAd) {
            assertTrue { id.asString().isNotBlank() }
            assertEquals(expected.title, title)
            assertEquals(expected.description, description)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.dealSide, dealSide)
            assertEquals(expected.visibility, visibility)
        }
    }

    @Test
    fun readSuccessTest() {
        val repo = RepoAdInMemory(
            initObjects = listOf(Bolt.getModel())
        )
        val crud = AdCrud(config = ContextConfig(repoTest = repo))
        val context = MpContext(
            workMode = WorkMode.TEST,
            requestAdId = Bolt.getModel().id,
            operation = MpContext.MpOperations.READ,
        )
        runBlocking {
            crud.read(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = Bolt.getModel()
        with(context.responseAd) {
            assertEquals(expected.id, id)
            assertEquals(expected.title, title)
            assertEquals(expected.description, description)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.dealSide, dealSide)
            assertEquals(expected.visibility, visibility)
        }
    }

    @Test
    fun updateSuccessTest() {
        val repo = RepoAdInMemory(
            initObjects = listOf(Bolt.getModel())
        )
        val crud = AdCrud(config = ContextConfig(repoTest = repo))
        val context = MpContext(
            workMode = WorkMode.TEST,
            requestAd = Bolt.getModel(),
            operation = MpContext.MpOperations.UPDATE,
        )
        runBlocking {
            crud.update(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = Bolt.getModel()
        with(context.responseAd) {
            assertEquals(expected.id, id)
            assertEquals(expected.title, title)
            assertEquals(expected.description, description)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.dealSide, dealSide)
            assertEquals(expected.visibility, visibility)
        }
    }

    @Test
    fun deleteSuccessTest() {
        val repo = RepoAdInMemory(
            initObjects = listOf(Bolt.getModel())
        )
        val crud = AdCrud(config = ContextConfig(repoTest = repo))
        val context = MpContext(
            workMode = WorkMode.TEST,
            requestAdId = Bolt.getModel().id,
            operation = MpContext.MpOperations.DELETE,
        )
        runBlocking {
            crud.delete(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = Bolt.getModel()
        with(context.responseAd) {
            assertEquals(expected.id, id)
            assertEquals(expected.title, title)
            assertEquals(expected.description, description)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.dealSide, dealSide)
            assertEquals(expected.visibility, visibility)
        }
    }

    @Test
    fun searchSuccessTest() {
        val repo = RepoAdInMemory(
            initObjects = listOf(
                Bolt.getModel {
                    id = AdIdModel("123")
                    dealSide = DealSideModel.DEMAND
                },
                Bolt.getModel {
                    id = AdIdModel("124")
                    dealSide = DealSideModel.DEMAND
                },
                Bolt.getModel {
                    id = AdIdModel("125")
                    dealSide = DealSideModel.PROPOSAL
                },
                Bolt.getModel {
                    id = AdIdModel("126")
                    dealSide = DealSideModel.DEMAND
                },
                Bolt.getModel {
                    id = AdIdModel("127")
                    dealSide = DealSideModel.PROPOSAL
                },
            )
        )
        val crud = AdCrud(config = ContextConfig(repoTest = repo))
        val context = MpContext(
            workMode = WorkMode.TEST,
            requestFilter = DbAdFilterRequest(dealSide = DealSideModel.PROPOSAL),
            requestPage = PaginatedModel(),
            operation = MpContext.MpOperations.SEARCH,
        )
        runBlocking {
            crud.search(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        with(context.responseAds) {
            assertEquals(2, size)
            assertEquals(listOf("125", "127"), map { it.id.asString() }.sorted())
        }
    }

    @Test
    fun offersSuccessTest() {
        val repo = RepoAdInMemory(
            initObjects = listOf(
                Bolt.getModel {
                    id = AdIdModel("123")
                    dealSide = DealSideModel.DEMAND
                },
                Bolt.getModel {
                    id = AdIdModel("124")
                    dealSide = DealSideModel.DEMAND
                },
                Bolt.getModel {
                    id = AdIdModel("125")
                    dealSide = DealSideModel.PROPOSAL
                },
                Bolt.getModel {
                    id = AdIdModel("126")
                    dealSide = DealSideModel.DEMAND
                },
                Bolt.getModel {
                    id = AdIdModel("127")
                    dealSide = DealSideModel.PROPOSAL
                },
            )
        )
        val crud = AdCrud(config = ContextConfig(repoTest = repo))
        val context = MpContext(
            workMode = WorkMode.TEST,
            requestPage = PaginatedModel(),
            requestAdId = AdIdModel("126"),
            operation = MpContext.MpOperations.OFFER
        )
        runBlocking {
            crud.offer(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = Bolt.getModels()
        with(context.responseAds) {
            assertEquals(expected.size, size)
        }
    }
}
