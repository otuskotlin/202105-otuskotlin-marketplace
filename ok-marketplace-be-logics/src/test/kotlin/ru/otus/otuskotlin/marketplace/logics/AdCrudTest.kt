package ru.otus.otuskotlin.marketplace.logics

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.AdIdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MpStubCase
import ru.otus.otuskotlin.marketplace.backend.common.models.PaginatedModel
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
class AdCrudTest {
    @Test
    fun initTest() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.SUCCESS,
            operation = MpContext.MpOperations.INIT,
        )
        runBlocking {
            crud.init(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
    }

    @Test
    fun createSuccessTest() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.SUCCESS,
            requestAd = Bolt.getModel { id = AdIdModel.NONE },
            operation = MpContext.MpOperations.CREATE,
        )
        runBlocking {
            crud.create(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = Bolt.getModel()
        with(context.responseAd) {
            assertEquals(expected.id, id)
            assertEquals(expected.title, title)
            assertEquals(expected.description, description)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.dealSide, dealSide)
            assertEquals(expected.permissions, permissions)
            assertEquals(expected.visibility, visibility)
        }
    }

    @Test
    fun readSuccessTest() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.SUCCESS,
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
            assertEquals(expected.permissions, permissions)
            assertEquals(expected.visibility, visibility)
        }
    }

    @Test
    fun updateSuccessTest() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.SUCCESS,
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
            assertEquals(expected.permissions, permissions)
            assertEquals(expected.visibility, visibility)
        }
    }

    @Test
    fun deleteSuccessTest() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.SUCCESS,
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
            assertEquals(expected.permissions, permissions)
            assertEquals(expected.visibility, visibility)
        }
    }

    @Test
    fun searchSuccessTest() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.SUCCESS,
            requestPage = PaginatedModel(),
            operation = MpContext.MpOperations.SEARCH,
        )
        runBlocking {
            crud.search(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = Bolt.getModels()
        with(context.responseAds) {
            assertEquals(expected.size, size)
        }
    }

    @Test
    fun offersSuccessTest() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.SUCCESS,
            requestPage = PaginatedModel(),
            requestAdId = Bolt.getModel().id,
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
