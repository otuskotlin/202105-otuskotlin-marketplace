package ru.otus.otuskotlin.marketplace.backend.common.models

data class CommonErrorModel(
    override val field: String = "",
    override val level: IError.Level = IError.Level.ERROR,
    override val message: String = "",
): IError
