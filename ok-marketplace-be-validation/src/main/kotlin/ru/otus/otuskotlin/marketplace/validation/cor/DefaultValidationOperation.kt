package ru.otus.otuskotlin.marketplace.validation.cor

import ru.otus.otuskotlin.marketplace.validation.IValidator
import ru.otus.otuskotlin.marketplace.validation.ValidationResult

class DefaultValidationOperation<C, T>(
    private val onBlock: C.() -> T,
    private val validator: IValidator<T>,
    private var errorHandler: C.(ValidationResult) -> Unit = {}
): IValidationOperation<C, T> {
    override fun exec(context: C) {
        val value = context.onBlock()
        val res = validator.validate(value)
        context.errorHandler(res)
    }

}
