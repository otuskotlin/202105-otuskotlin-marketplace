package ru.otus.otuskotlin.marketplace.validation.cor

import ru.otus.otuskotlin.marketplace.validation.ValidationResult

class ValidationBuilder<C> {
    private var errorHandler: C.(ValidationResult) -> Unit = {}
    private val validators: MutableList<IValidationOperation<C, *>> = mutableListOf()

    fun errorHandler(block: C.(ValidationResult) -> Unit) {
        errorHandler = block
    }

    fun <T> validate(block: ValidationOperationBuilder<C, T>.() -> Unit) {
        val builder = ValidationOperationBuilder<C, T>(errorHandler).apply(block)
        validators.add(builder.build())
    }

    fun build() = PipelineValidation(validators)

}
