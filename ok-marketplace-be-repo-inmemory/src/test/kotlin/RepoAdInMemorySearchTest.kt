import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdSearchTest

class RepoAdInMemorySearchTest : RepoAdSearchTest() {
    override val repo: IRepoAd = RepoAdInMemory(
        initObjects = initObjects
    )
}
