package ru.otus.otuskotlin.marketplace.backend.common.models

@JvmInline
value class AdIdModel(private val id: String) {
    companion object {
        val NONE = AdIdModel("")
    }

    fun asString() = id
}
