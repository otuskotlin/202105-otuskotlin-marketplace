package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserGroups
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserPermissions
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

fun ICorChainDsl<MpContext>.chainPermissions(title: String) = worker<MpContext> {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on {
        status == CorStatus.RUNNING
    }

    handle {
        val permAdd: Set<MpUserPermissions> = principal.groups.map {
            when(it) {
                MpUserGroups.USER -> setOf(
                    MpUserPermissions.READ_OWN,
                    MpUserPermissions.READ_PUBLIC,
                    MpUserPermissions.CREATE_OWN,
                    MpUserPermissions.UPDATE_OWN,
                    MpUserPermissions.DELETE_OWN,
                )
                MpUserGroups.MODERATOR_MP -> setOf()
                MpUserGroups.ADMIN_AD -> setOf()
                MpUserGroups.TEST -> setOf()
                MpUserGroups.BAN_AD -> setOf()
            }
        }.flatten().toSet()
        val permDel: Set<MpUserPermissions> = principal.groups.map {
            when(it) {
                MpUserGroups.USER -> setOf()
                MpUserGroups.MODERATOR_MP -> setOf()
                MpUserGroups.ADMIN_AD -> setOf()
                MpUserGroups.TEST -> setOf()
                MpUserGroups.BAN_AD -> setOf(
                    MpUserPermissions.UPDATE_OWN,
                    MpUserPermissions.CREATE_OWN,
                )
            }
        }.flatten().toSet()
        chainPermissions.addAll(permAdd)
        chainPermissions.removeAll(permDel)
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $chainPermissions")
    }
}
