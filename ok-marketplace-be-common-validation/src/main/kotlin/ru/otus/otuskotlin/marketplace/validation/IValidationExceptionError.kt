package ru.otus.otuskotlin.marketplace.validation

interface IValidationExceptionError: IValidationError {
    val exception: Throwable
}
