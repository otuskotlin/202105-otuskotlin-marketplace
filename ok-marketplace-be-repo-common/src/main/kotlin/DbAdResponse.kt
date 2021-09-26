package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel

class DbAdResponse(
    override val result: AdModel?,
    override val isSuccess: Boolean,
    override val errors: List<CommonErrorModel>
) : IDbResponse<AdModel?>
