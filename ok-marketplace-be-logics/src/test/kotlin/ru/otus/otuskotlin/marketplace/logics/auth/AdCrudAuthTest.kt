package ru.otus.otuskotlin.marketplace.logics.auth

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.backend.common.context.ContextConfig
import ru.otus.otuskotlin.marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.logics.AdCrud
import ru.otus.otuskotlin.marketplace.logics.helpers.principalUser
import kotlin.test.*

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
class AdCrudAuthTest {

    @Test
    fun createSuccessTest() {
        val repo = RepoAdInMemory()
        val crud = AdCrud(
            config = ContextConfig(
                repoTest = repo
            )
        )
        val context = MpContext(
            workMode = WorkMode.TEST,
            requestAd = Bolt.getModel { id = AdIdModel.NONE },
            operation = MpContext.MpOperations.CREATE,
            principal = principalUser()
        )
        runBlocking {
            crud.create(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        with(context.responseAd) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(context.responseAd.permissions, PermissionModel.READ)
            assertContains(context.responseAd.permissions, PermissionModel.UPDATE)
            assertContains(context.responseAd.permissions, PermissionModel.DELETE)
            assertFalse { context.responseAd.permissions.contains(PermissionModel.CONTACT) }
        }
    }

    @Test
    fun readSuccessTest() {
        val repo = RepoAdInMemory(
            listOf(Bolt.getModel())
        )
        val crud = AdCrud(
            config = ContextConfig(
                repoTest = repo
            )
        )
        val context = MpContext(
            workMode = WorkMode.TEST,
            requestAdId = Bolt.getModel().id,
            operation = MpContext.MpOperations.READ,
            principal = principalUser()
        )
        runBlocking {
            crud.read(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        with(context.responseAd) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(context.responseAd.permissions, PermissionModel.READ)
            assertContains(context.responseAd.permissions, PermissionModel.UPDATE)
            assertContains(context.responseAd.permissions, PermissionModel.DELETE)
            assertFalse { context.responseAd.permissions.contains(PermissionModel.CONTACT) }
        }
    }

}
