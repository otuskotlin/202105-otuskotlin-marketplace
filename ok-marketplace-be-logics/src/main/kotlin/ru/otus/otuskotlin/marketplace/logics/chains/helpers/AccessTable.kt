package ru.otus.otuskotlin.marketplace.logics.chains.helpers

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.MpPrincipalRelations
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserPermissions

data class AccessTableConditions(
    val operation: MpContext.MpOperations,
    val permission: MpUserPermissions,
    val relation: MpPrincipalRelations
)

val accessTable = mapOf(
    // Create
    AccessTableConditions(
        operation = MpContext.MpOperations.CREATE,
        permission = MpUserPermissions.CREATE_OWN,
        relation = MpPrincipalRelations.OWN
    ) to true,

    // Read
    AccessTableConditions(
        operation = MpContext.MpOperations.READ,
        permission = MpUserPermissions.READ_OWN,
        relation = MpPrincipalRelations.OWN
    ) to true,
    AccessTableConditions(
        operation = MpContext.MpOperations.READ,
        permission = MpUserPermissions.READ_PUBLIC,
        relation = MpPrincipalRelations.PUBLIC
    ) to true,
)
