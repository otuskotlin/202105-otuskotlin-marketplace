package ru.otus.otuskotlin.marketplace.backend.common.models

data class CommonErrorModel(
    override var field: String = "",
    override var level: IError.Level = IError.Level.ERROR,
    override var message: String = "",
): IError
