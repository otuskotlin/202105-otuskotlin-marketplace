package homework.hard.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Meaning(
    val definitions: List<Definition>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Definition(
    val definition: String,
    val example: String? = ""
)