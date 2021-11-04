package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import ru.otus.otuskotlin.marketplace.backend.repo.test.*
import java.util.*

/*
Никогда так не делайте как в этом тесте.

Здесь используется боевая база данных dynamoDB с тестовыми таблицами.

В итоге, таблицы создаются после каждой сборки. Если их не подчищать, может набежать довольно крупная сумма за
использование dynamoDB в AWS.

Тестирование необходимо выполнять с помощью тестового Docker-образа dynamoDb:
https://hub.docker.com/r/amazon/dynamodb-local

 */

//class RepoAdDynamoCreateTest: RepoAdCreateTest() {
//    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
//}
//
//class RepoAdDynamoReadTest: RepoAdReadTest() {
//    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
//}
//
//class RepoAdDynamoUpdateTest: RepoAdUpdateTest() {
//    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
//}
//
//class RepoAdDynamoDeleteTest: RepoAdDeleteTest() {
//    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
//}
//
//// Не проходит с глобальными индексами, индекс не успевает обновиться
//class RepoAdDynamoSearchTest: RepoAdSearchTest() {
//    override val repo = RepoAdDynamo(initObjects = initObjects, table = "test-${UUID.randomUUID()}")
//}
