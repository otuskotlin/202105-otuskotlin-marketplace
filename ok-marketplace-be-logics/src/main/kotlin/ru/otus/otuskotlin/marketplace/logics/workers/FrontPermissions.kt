package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.CorStatus
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserGroups
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserPermissions
import ru.otus.otuskotlin.marketplace.backend.common.models.PermissionModel
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.cor.worker

fun ICorChainDsl<MpContext>.frontPermissions(title: String) = chain<MpContext> {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { status == CorStatus.RUNNING }

    worker {
        this.title = "Разрешения для собственного объявления"
        description = this.title
        on { responseAd.ownerId == principal.id }
        handle {
            val permAdd: Set<PermissionModel> = principal.groups.map {
                when (it) {
                    MpUserGroups.USER -> setOf(
                        PermissionModel.READ,
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                    )
                    MpUserGroups.MODERATOR_MP -> setOf()
                    MpUserGroups.ADMIN_AD -> setOf()
                    MpUserGroups.TEST -> setOf()
                    MpUserGroups.BAN_AD -> setOf()
                }
            }.flatten().toSet()
            val permDel: Set<PermissionModel> = principal.groups.map {
                when (it) {
                    MpUserGroups.USER -> setOf()
                    MpUserGroups.MODERATOR_MP -> setOf()
                    MpUserGroups.ADMIN_AD -> setOf()
                    MpUserGroups.TEST -> setOf()
                    MpUserGroups.BAN_AD -> setOf(
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                    )
                }
            }.flatten().toSet()
            responseAd.permissions.addAll(permAdd)
            responseAd.permissions.removeAll(permDel)
        }
    }

    worker {
        this.title = "Разрешения для модератора"
        description = this.title
        on { responseAd.ownerId != principal.id /* && tag, group, ... */ }
        handle {
            val permAdd: Set<PermissionModel> = principal.groups.map {
                when (it) {
                    MpUserGroups.USER -> setOf()
                    MpUserGroups.MODERATOR_MP -> setOf(
                        PermissionModel.READ,
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                        PermissionModel.CONTACT,
                    )
                    MpUserGroups.ADMIN_AD -> setOf()
                    MpUserGroups.TEST -> setOf()
                    MpUserGroups.BAN_AD -> setOf()
                }
            }.flatten().toSet()
            val permDel: Set<PermissionModel> = principal.groups.map {
                when (it) {
                    MpUserGroups.USER -> setOf(
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                        PermissionModel.CONTACT,
                    )
                    MpUserGroups.MODERATOR_MP -> setOf()
                    MpUserGroups.ADMIN_AD -> setOf()
                    MpUserGroups.TEST -> setOf()
                    MpUserGroups.BAN_AD -> setOf(
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                        PermissionModel.CONTACT,
                    )
                }
            }.flatten().toSet()
            responseAd.permissions.addAll(permAdd)
            responseAd.permissions.removeAll(permDel)
        }
    }
    worker {
        this.title = "Разрешения для администратора"
        description = this.title
    }
}
