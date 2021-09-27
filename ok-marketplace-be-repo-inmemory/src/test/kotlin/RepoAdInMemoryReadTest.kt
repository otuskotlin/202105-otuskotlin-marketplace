import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdReadTest

class RepoAdInMemoryReadTest : RepoAdReadTest() {
    override val repo: IRepoAd = RepoAdInMemory(
        initObjects = initObjects
    )
}
