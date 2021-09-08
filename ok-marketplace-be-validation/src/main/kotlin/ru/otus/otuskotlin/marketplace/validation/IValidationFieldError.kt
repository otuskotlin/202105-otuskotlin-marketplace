package ru.otus.otuskotlin.marketplace.validation

interface IValidationFieldError : IValidationError {
    val field: String
}
