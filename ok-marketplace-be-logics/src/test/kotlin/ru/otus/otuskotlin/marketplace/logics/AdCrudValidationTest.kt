package ru.otus.otuskotlin.marketplace.logics

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.AdIdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MpStubCase
import ru.otus.otuskotlin.marketplace.stubs.Bolt
import kotlin.test.assertEquals

class AdCrudValidationTest {
    @Test
    fun `create success`() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.SUCCESS,
            requestAd = Bolt.getModel { id = AdIdModel("11111111-1111-1111-1111-111111111111") },
            operation = MpContext.MpOperations.CREATE,
        )
        runBlocking {
            crud.create(context)
        }
        println(context.status)
        println(context.errors)
        println(context.requestAd)
        assertEquals(CorStatus.SUCCESS, context.status)

        assertEquals(CorStatus.SUCCESS, context.status)
        assertEquals(0, context.errors.size)
    }

    @Test
    fun `create failing`() {
        val crud = AdCrud()
        val context = MpContext(
            stubCase = MpStubCase.DATABASE_ERROR,
            requestAd = Bolt.getModel { id = AdIdModel.NONE },
            operation = MpContext.MpOperations.CREATE,
        )
        runBlocking {
            crud.create(context)
        }

        assertEquals(CorStatus.ERROR, context.status)
        assertEquals(1, context.errors.size)
    }

}
