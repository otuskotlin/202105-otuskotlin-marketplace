package ru.otus.otuskotlin.marketplace.backend.common.models

import java.util.*

@JvmInline
value class AdIdModel(private val id: String) {
    constructor(id: UUID) : this(id.toString())

    companion object {
        val NONE = AdIdModel("")
    }

    fun asString() = id

    fun asUUID(): UUID = UUID.fromString(id)
}
