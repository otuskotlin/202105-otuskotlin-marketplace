package ru.otus.otuskotlin.marketplace.logics.chains.helpers

import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.AdVisibilityModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MpPrincipalModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MpPrincipalRelations

fun AdModel.resolveRelationsTo(principal: MpPrincipalModel): Set<MpPrincipalRelations> = listOf(
    MpPrincipalRelations.NONE,
    MpPrincipalRelations.OWN.takeIf { principal.id == ownerId },
    MpPrincipalRelations.PUBLIC.takeIf { visibility == AdVisibilityModel.PUBLIC },
    MpPrincipalRelations.MODERATABLE.takeIf { visibility != AdVisibilityModel.OWNER_ONLY },
).filterNotNull().toSet()
