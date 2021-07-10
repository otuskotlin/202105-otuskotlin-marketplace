import kotlin.math.roundToInt

fun main() {
    val distribution = BoxMullerDistribution().scale(50.0, 1.5)
    distribution
        .take(10000)
        .groupingBy { it.roundToInt() }
        .eachCount()
        .toList()
        .sortedBy { it.first }
        .forEach { (num, count) ->
            println("$num: " + "*".repeat(count / 100))
        }
}