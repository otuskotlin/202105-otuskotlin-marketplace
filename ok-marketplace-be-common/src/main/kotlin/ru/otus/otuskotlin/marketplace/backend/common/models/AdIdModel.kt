package ru.otus.otuskotlin.marketplace.backend.common.models

inline class AdIdModel(val id: String) {
    companion object {
        val NONE = AdIdModel("")
    }

    fun asString() = id
}
