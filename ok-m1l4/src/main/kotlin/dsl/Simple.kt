package dsl

fun sout(block: () -> Any) {
    val value = block()
    println("Ans: $value")
}

class Context {
    fun time() = System.currentTimeMillis()
}

fun soutWithPrefix(blockWithContext: Context.() -> Any) {
    val context = Context()
    val value = blockWithContext(context)
    println("Ans: $value")
}
