package ru.otus.otuskotlin.marketplace.logics.workers

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.common.models.MpStubCase
import ru.otus.otuskotlin.marketplace.backend.common.models.MpUserGroups
import ru.otus.otuskotlin.marketplace.backend.common.models.WorkMode
import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.common.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker

internal fun ICorChainDsl<MpContext>.chooseDb(title: String) = worker {
    this.title = title
    description = """
        Here we choose either prod or test DB repository. 
        In case of STUB request here we use empty repo and set stubCase=SUCCESS if unset
    """.trimIndent()

    handle {
        println("chooseDb")
        if (principal.groups.contains(MpUserGroups.TEST)) {
            adRepo = config.repoTest
            return@handle
        }
        adRepo = when(workMode) {
            WorkMode.PROD -> config.repoProd
            WorkMode.TEST -> config.repoTest
            WorkMode.STUB -> {
                if (stubCase == MpStubCase.NONE){
                    stubCase = MpStubCase.SUCCESS
                }
                IRepoAd.NONE
            }
        }
    }
}
