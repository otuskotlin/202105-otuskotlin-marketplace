package homework

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import homework.dto.GithubRepo
import homework.dto.GithubUser
import okhttp3.Response

class GithubApi(
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) {

    private fun get(uri: String) =
        HttpClient(Authorization).get(uri)

    fun login(): GithubUser {
        println("Requesting $LOGIN_URI")
        val response = get(LOGIN_URI)

        return getBody(response)
    }

    fun getRepository(repoUri: String): GithubRepo {
        println("Requesting $repoUri")
        val response = get(repoUri)
        return getBody(response)
    }

    private inline fun <reified T> getBody(response: Response): T {
        if (!response.isSuccessful) {
            throw RuntimeException("Invalid login or password")
        }

        return response.body?.let {
            objectMapper.readValue<T>(it.string())
        } ?: throw RuntimeException("Body is null by some reason")
    }
}