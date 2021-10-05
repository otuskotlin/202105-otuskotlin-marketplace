package ru.otus.otuskotlin.marketplace.backend.repo.sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.DEFAULT_ISOLATION_LEVEL
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

class SqlConnector(
    private val properties: Properties = loadFromFile("datasource.properties"),
    private val databaseConfig: DatabaseConfig = DatabaseConfig { defaultIsolationLevel = DEFAULT_ISOLATION_LEVEL }
) {
    private enum class DbType(val driver: String) {
        MYSQL("com.mysql.cj.jdbc.Driver"),
        POSTGRESQL("org.postgresql.Driver")
    }

    private val dbType: DbType = properties.getProperty(SqlHostingProperties.SQL_URL).let { url ->
        when {
            url.startsWith("jdbc:mysql://") -> DbType.MYSQL
            url.startsWith("jdbc:postgresql://") -> DbType.POSTGRESQL
            else -> error("Cannot parse database type from url: $url")
        }
    }.also { properties.setProperty("driverClassName", it.driver) }

    private val dataSource = object : HikariDataSource(HikariConfig(properties.withoutCustom())) {
        override fun getConnection(): Connection {
            return object : Connection by super.getConnection() {
                override fun setAutoCommit(autoCommit: Boolean) {
                    if (autoCommit) {
                        throw UnsupportedOperationException("Can't turn auto-commit on, it's set across the Hikari Pool")
                    }
                }
            }
        }
    }

    private val globalConnection = Database.connect(dataSource, databaseConfig = databaseConfig)

    fun connect(vararg tables: Table): Database {
        val schemaName = dataSource.schema

        // Create/update schema
        transaction(globalConnection) {
            when (dbType) {
                DbType.MYSQL -> {
                    val dbSettings = " DEFAULT CHARACTER SET utf8mb4\n" +
                            " DEFAULT COLLATE utf8mb4_general_ci"
                    connection.prepareStatement("CREATE DATABASE IF NOT EXISTS $schemaName\n$dbSettings", false)
                        .executeUpdate()
                    connection.prepareStatement("ALTER DATABASE $schemaName\n$dbSettings", false).executeUpdate()
                }
                DbType.POSTGRESQL -> {
                    connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS $schemaName", false).executeUpdate()
                }
            }
        }

        // Create connection
        val connect = Database.connect(
            dataSource,
            databaseConfig = databaseConfig,
            setupConnection = { connection ->
                when (dbType) {
                    DbType.MYSQL -> {
                        connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                        connection.schema = schemaName
                        connection.catalog = schemaName
                    }
                    DbType.POSTGRESQL -> {
                        connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                        connection.schema = schemaName
                    }
                }
            }
        )

        // Update schema
        transaction(connect) {
            if (properties.getProperty(SqlHostingProperties.SQL_DROP_DB, "false").toBoolean()) {
                SchemaUtils.drop(*tables, inBatch = true)
                SchemaUtils.create(*tables, inBatch = true)
            } else if (properties.getProperty(SqlHostingProperties.SQL_FAST_MIGRATION, "true").toBoolean()) {
                // TODO: Place to exec migration: create and ensure tables
            } else {
                SchemaUtils.createMissingTablesAndColumns(*tables, inBatch = true)
            }
        }

        return connect
    }
}
