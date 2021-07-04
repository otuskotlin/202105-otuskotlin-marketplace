package structuredconcurency

import kotlinx.coroutines.delay

class Vocabulary {
    private val words = listOf("сильный", "могущественный", "невозмутимый", "любимый")

    suspend fun find(word: String, withTime: Long = 2000): String {
        delay(withTime)
        return words.find { it.matches(Regex(pattern = "${word}.*$")) } ?: throw RuntimeException("Word $word not found in dict")
            .also {
                println("Done searching $word")
            }

    }
}