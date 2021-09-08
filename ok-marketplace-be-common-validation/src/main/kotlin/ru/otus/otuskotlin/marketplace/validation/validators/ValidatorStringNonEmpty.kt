package ru.otus.otuskotlin.marketplace.validation.validators

import ru.otus.otuskotlin.marketplace.validation.IValidator
import ru.otus.otuskotlin.marketplace.validation.ValidationFieldError
import ru.otus.otuskotlin.marketplace.validation.ValidationResult

class ValidatorStringNonEmpty(
    private val field: String = "",
    private val message: String = "String must not be empty"
): IValidator<String?> {

    override fun validate(sample: String?): ValidationResult {
        return if (sample.isNullOrBlank()) {
            ValidationResult(
                errors = listOf(
                    ValidationFieldError(
                        field = field,
                        message = message,
                    )
                )
            )
        } else {
            ValidationResult.SUCCESS
        }
    }

}
