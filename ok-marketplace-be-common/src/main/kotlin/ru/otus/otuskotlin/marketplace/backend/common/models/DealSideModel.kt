package ru.otus.otuskotlin.marketplace.backend.common.models

enum class DealSideModel {
    NONE,
    DEMAND,
    PROPOSAL;

    fun opposite(): DealSideModel = when(this) {
        DEMAND -> PROPOSAL
        PROPOSAL -> DEMAND
        NONE -> NONE
    }
}
