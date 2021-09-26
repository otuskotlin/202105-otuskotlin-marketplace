import ru.otus.otuskotlin.marketplace.backend.repo.common.IRepoAd
import ru.otus.otuskotlin.marketplace.backend.repo.inmemory.RepoAdInMemory
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdUpdateTest

class RepoAdInMemoryUpdateTest : RepoAdUpdateTest() {
    override val repo: IRepoAd = RepoAdInMemory(
        initObjects = initObjects
    )
}
