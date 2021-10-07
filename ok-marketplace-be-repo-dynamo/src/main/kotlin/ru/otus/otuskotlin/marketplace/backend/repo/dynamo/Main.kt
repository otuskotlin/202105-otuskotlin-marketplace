package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import kotlinx.coroutines.*
import ru.otus.otuskotlin.marketplace.backend.common.models.AdIdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.AdModel
import ru.otus.otuskotlin.marketplace.backend.common.models.DealSideModel
import ru.otus.otuskotlin.marketplace.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdModelRequest

fun main() {
    val repo = RepoAdDynamo(test = true)
    runBlocking {
        println("START")
//        repo.init()
        val created = repo.create(
            rq = DbAdModelRequest(
                ad = AdModel(
                    title = "first-ad",
                    description = "some-description"
                )
            )
        )
        repo.create(
            rq = DbAdModelRequest(
                ad = AdModel(
                    title = "second-ad",
                    description = "some-description",
                    ownerId = OwnerIdModel("1"),
                    dealSide = DealSideModel.DEMAND
                )
            )
        )
        repo.create(
            rq = DbAdModelRequest(
                ad = AdModel(
                    title = "third-ad",
                    description = "some-description",
                    ownerId = OwnerIdModel("1"),
                    dealSide = DealSideModel.DEMAND
                )
            )
        )
        repo.create(
            rq = DbAdModelRequest(
                ad = AdModel(
                    title = "fourth-ad",
                    description = "some-description",
                    ownerId = OwnerIdModel("1"),
                    dealSide = DealSideModel.DEMAND
                )
            )
        )
        println("${created.result} \n ${created.errors}")
        delay(1000)
        val received = repo.read(
            rq = DbAdIdRequest(
                id = created.result?.id ?: AdIdModel.NONE
            )
        )
        println("${received.result} \n ${received.errors}")
        val list = repo.search(
            rq = DbAdFilterRequest(
                ownerId = OwnerIdModel("1"),
                dealSide = DealSideModel.DEMAND
            )
        )
        println(list.result)
        try {
            repo.clean()
            delay(3000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
