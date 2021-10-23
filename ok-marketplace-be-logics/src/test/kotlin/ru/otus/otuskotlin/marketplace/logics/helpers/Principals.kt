package ru.otus.otuskotlin.marketplace.logics.helpers

import ru.otus.otuskotlin.marketplace.backend.common.models.MpPrincipalModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserGroups
import ru.otus.otuskotlin.marketplace.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.marketplace.stubs.Bolt

fun principalUser(id: OwnerIdModel = Bolt.getModel().ownerId, banned: Boolean = false) = MpPrincipalModel(
    id = id,
    groups = setOf(
        MpUserGroups.USER,
        MpUserGroups.TEST,
        if (banned) MpUserGroups.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)

fun principalModer(id: OwnerIdModel = Bolt.getModel().ownerId, banned: Boolean = false) = MpPrincipalModel(
    id = id,
    groups = setOf(
        MpUserGroups.MODERATOR_MP,
        MpUserGroups.TEST,
        if (banned) MpUserGroups.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)

fun principalAdmin(id: OwnerIdModel = Bolt.getModel().ownerId, banned: Boolean = false) = MpPrincipalModel(
    id = id,
    groups = setOf(
        MpUserGroups.ADMIN_AD,
        MpUserGroups.TEST,
        if (banned) MpUserGroups.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)
