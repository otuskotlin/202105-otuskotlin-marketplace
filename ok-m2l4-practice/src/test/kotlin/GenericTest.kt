import kotlin.test.Test

class GenericTest {
    @Test
    fun genericIn() {
        val handler = SomeHandler<SomeOne>()
        var variable : List<String>
    }
}

interface ISome {
    val str: String
}

data class SomeOne(
    override var str: String
) : ISome

class SomeHandler<T> where T: ISome {
    fun handle(x: T): T {
        println("res $x")
        return x
    }
}
