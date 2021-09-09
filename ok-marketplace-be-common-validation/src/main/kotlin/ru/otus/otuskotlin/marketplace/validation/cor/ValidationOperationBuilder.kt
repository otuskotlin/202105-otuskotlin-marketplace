package ru.otus.otuskotlin.marketplace.validation.cor

import ru.otus.otuskotlin.marketplace.common.cor.CorDslMarker
import ru.otus.otuskotlin.marketplace.validation.IValidator
import ru.otus.otuskotlin.marketplace.validation.ValidationResult

@CorDslMarker
class ValidationOperationBuilder<C, T>(
    private var errorHandler: C.(ValidationResult) -> Unit = {}
) {
    private lateinit var onBlock: C.() -> T
    private lateinit var validator: IValidator<T>
    fun validator(validator: IValidator<T>) {
        this.validator = validator
    }
    fun on(block: C.() -> T) {
        onBlock = block
    }
    fun build(): IValidationOperation<C, T> {
        return DefaultValidationOperation(
            validator = validator,
            onBlock = onBlock,
            errorHandler = errorHandler
        )
    }
}
