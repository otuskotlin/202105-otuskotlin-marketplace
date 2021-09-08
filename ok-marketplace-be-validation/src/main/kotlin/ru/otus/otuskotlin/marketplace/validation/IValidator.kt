package ru.otus.otuskotlin.marketplace.validation

interface IValidator<T> {
    infix fun validate(sample: T): ValidationResult
}
