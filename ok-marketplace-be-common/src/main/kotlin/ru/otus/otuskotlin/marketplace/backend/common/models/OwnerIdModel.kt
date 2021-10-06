package ru.otus.otuskotlin.marketplace.backend.common.models

import java.util.*

@JvmInline
value class OwnerIdModel(val id: String) {
    constructor(id: UUID) : this(id.toString())

    companion object {
        val NONE = OwnerIdModel("")
    }

    fun asString() = id

    fun asUUID(): UUID = UUID.fromString(id)
}
