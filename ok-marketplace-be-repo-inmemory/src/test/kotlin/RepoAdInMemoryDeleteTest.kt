import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdDeleteTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdUpdateTest

class RepoAdInMemoryDeleteTest : RepoAdDeleteTest() {
    override val repo: IRepoAd = RepoAdInMemory(
        initObjects = initObjects
    )
}
