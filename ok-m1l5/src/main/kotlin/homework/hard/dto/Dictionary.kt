package homework.hard.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Dictionary(
    val word: String,
    val meanings: List<Meaning>
)
