package ru.otus.otuskotlin.marketplace.backend.common.models

interface IError {
    val field: String
    val level: Level
    val message: String
    val exception: Throwable

    enum class Level {
        ERROR,
        WARNING
    }
}
