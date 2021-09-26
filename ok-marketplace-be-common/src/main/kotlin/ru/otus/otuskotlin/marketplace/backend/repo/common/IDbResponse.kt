package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel

interface IDbResponse<T> {
    val isSuccess: Boolean
    val errors: List<CommonErrorModel>
    val result: T
}
