package ru.otus.otuskotlin.marketplace.backend.repo.sql.tables

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.otus.otuskotlin.marketplace.backend.common.models.*

object AdTable : Table("Ads") {
    val id = uuid("id").autoGenerate()
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val ownerId = uuid("ownerId")
    val visibility = enumeration("visibility", AdVisibilityModel::class)
    val dealSide = enumeration("dealSide", DealSideModel::class)

    val isDeleted = bool("is_deleted").nullable()

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

fun AdTable.selectNotDeleted(where: SqlExpressionBuilder.() -> Op<Boolean>) = select(
    SqlExpressionBuilder.where() and (isDeleted eq false)
)
