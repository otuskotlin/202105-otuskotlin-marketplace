package ru.otus.otuskotlin.marketplace.mappers

import io.ktor.auth.jwt.*
import ru.otus.otuskotlin.marketplace.backend.common.models.MpPrincipalModel
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserGroups
import ru.otus.otuskotlin.marketplace.backend.common.models.OwnerIdModel

fun JWTPrincipal?.toModel() = this?.run {
    MpPrincipalModel(
        id = payload.getClaim("id").asString()?.let { OwnerIdModel(it) } ?: OwnerIdModel.NONE,
        fname = payload.getClaim("fname").asString() ?: "",
        mname = payload.getClaim("mname").asString() ?: "",
        lname = payload.getClaim("lname").asString() ?: "",
        groups = payload
            .getClaim("groups")
            ?.asList(String::class.java)
            ?.mapNotNull {
                try {
                    MpUserGroups.valueOf(it)
                } catch (e: Throwable) {
                    null
                }
            }?.toSet() ?: emptySet()
    )
} ?: MpPrincipalModel.NONE
