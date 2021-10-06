package ru.otus.otuskotlin.marketplace.backend.repo.sql

import org.testcontainers.containers.PostgreSQLContainer
import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "marketplace-pass"
    private const val SCHEMA = "marketplace"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(initObjects: Collection<AdModel> = emptyList()): RepoAdSQL {
        return RepoAdSQL(url, USER, PASS, SCHEMA, initObjects)
    }
}
