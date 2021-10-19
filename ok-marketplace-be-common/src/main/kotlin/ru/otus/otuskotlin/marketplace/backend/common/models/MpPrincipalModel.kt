package ru.otus.otuskotlin.marketplace.backend.common.models

data class MpPrincipalModel(
    val id: OwnerIdModel = OwnerIdModel.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: List<MpUserGroups> = emptyList()
) {
    companion object {
        val NONE = MpPrincipalModel()
    }
}
