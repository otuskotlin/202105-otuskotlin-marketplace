package ru.otus.otuskotlin.marketplace.backend.repo.common

import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.CommonErrorModel

class DbAdsResponse(
    override val result: List<AdModel>,
    override val isSuccess: Boolean,
    override val errors: List<CommonErrorModel>
) : IDbResponse<List<AdModel>>
