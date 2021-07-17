import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class KotestTestJvm: StringSpec({
    "first Test Jvm" {
        1.toString() shouldBe "1"
    }

    "maximum of two numbers" {
        forAll(
            row(1, 5, 5),
            row(1, 0, 1),
            row(0, 0, 0)
        ) { a, b, max ->
            Math.max(a, b) shouldBe max
        }
    }
})
