package ru.otus.otuskotlin.marketplace.backend.repo.sql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.otus.otuskotlin.marketplace.backend.common.models.*

object AdsTable : Table("Ads") {
    val id = uuid("id").autoGenerate().uniqueIndex()
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val ownerId = reference("owner_id", UsersTable.id).index()
    val visibility = enumeration("visibility", AdVisibilityModel::class)
    val dealSide = enumeration("deal_side", DealSideModel::class).index()

    override val primaryKey = PrimaryKey(id)

    // Mapper functions from sql-like table to AdModel
    fun from(res: InsertStatement<Number>) = AdModel(
        id = AdIdModel(res[id]),
        title = res[title],
        description = res[description],
        ownerId = OwnerIdModel(res[ownerId]),
        visibility = res[visibility],
        dealSide = res[dealSide]
    )

    fun from(res: ResultRow) = AdModel(
        id = AdIdModel(res[id]),
        title = res[title],
        description = res[description],
        ownerId = OwnerIdModel(res[ownerId]),
        visibility = res[visibility],
        dealSide = res[dealSide]
    )
}

object UsersTable : Table("Users") {
    val id = uuid("id").autoGenerate().uniqueIndex()
    // The field was created only for an example of using relationships and is not used anywhere,
    // but for the references, and must always be equals ID
    val name = varchar("name", 128)

    override val primaryKey = PrimaryKey(AdsTable.id)
}
