import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

fun boxMullerRandom(random: Random): Pair<Double, Double> {
    // Box-Muller algorithm to produce normal distributed numbers with mean = 0, std = 1
    // https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
    val u1 = random.nextDouble()
    val u2 = random.nextDouble()
    val z1 = sqrt(-2 * ln(u1)) * cos(2 * PI * u2)
    val z2 = sqrt(-2 * ln(u1)) * sin(2 * PI * u2)
    return z1 to z2
}

class BoxMullerDistribution(
    private val random: Random = Random.Default
) : Sequence<Double> by sequence({
    while (true) {
        val (z1, z2) = boxMullerRandom(random)
        yield(z1)
        yield(z2)
    }
}) {
    fun scale(mean: Double, std: Double): Sequence<Double> =
        map { it * std + mean }
}

fun main() {
    val values = BoxMullerDistribution()
        .scale(24.0, 1.5)
        .take(10000)
        .toList()

    values
        .groupingBy { it.roundToInt() }
        .eachCount()
        .toList()
        .sortedBy { it.first }
        .forEach { println(it) }
}