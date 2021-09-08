package ru.otus.otuskotlin.marketplace.validation.cor

interface IValidationOperation<C, T> {
    fun exec(context: C)
}

