package ru.otus.otuskotlin.marketplace.backend.common.models

inline class OwnerIdModel(val id: String) {
    companion object {
        val NONE = OwnerIdModel("")
    }

    fun asString() = id
}
