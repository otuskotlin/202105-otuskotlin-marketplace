import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdDeleteTest

class RepoAdInMemoryDeleteTest : RepoAdDeleteTest() {
    override val repo: IRepoAd = RepoAdInMemory(
        initObjects = initObjects
    )
}
