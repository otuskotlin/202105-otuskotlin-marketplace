package ru.otus.otuskotlin.marketplace.validation

data class ValidationDefaultError(
    override val message: String,
) : IValidationError
