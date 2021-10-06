package ru.otus.otuskotlin.marketplace.backend.repo.sql

import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.test.*

class RepoAdSQLCreateTest : RepoAdCreateTest() {
    override val repo: IRepoAd = RepoAdSQL(initObjects = initObjects)
}

class RepoAdSQLDeleteTest : RepoAdDeleteTest() {
    override val repo: IRepoAd = RepoAdSQL(initObjects = initObjects)
}

class RepoAdSQLReadTest : RepoAdReadTest() {
    override val repo: IRepoAd = RepoAdSQL(initObjects = initObjects)
}

class RepoAdSQLSearchTest : RepoAdSearchTest() {
    override val repo: IRepoAd = RepoAdSQL(initObjects = initObjects)
}

class RepoAdSQLUpdateTest : RepoAdUpdateTest() {
    override val repo: IRepoAd = RepoAdSQL(initObjects = initObjects)
}

