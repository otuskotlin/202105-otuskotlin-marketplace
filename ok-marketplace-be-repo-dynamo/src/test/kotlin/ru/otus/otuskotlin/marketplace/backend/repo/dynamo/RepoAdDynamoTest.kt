package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import ru.otus.otuskotlin.marketplace.backend.repo.test.*
import java.util.*

class RepoAdDynamoCreateTest: RepoAdCreateTest() {
    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
}

class RepoAdDynamoReadTest: RepoAdReadTest() {
    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
}

class RepoAdDynamoUpdateTest: RepoAdUpdateTest() {
    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
}

class RepoAdDynamoDeleteTest: RepoAdDeleteTest() {
    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
}

// Не проходит с глобальными индексами, индекс не успевает обновиться
class RepoAdDynamoSearchTest: RepoAdSearchTest() {
    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
}
