package homework

import homework.dto.GithubRepo

fun main() {
    val githubApi = GithubApi()
    val login = githubApi.login()
    println("Login successfully $login")

    val urls = listOf(OTUS_MARKETPLAGE_URI, KOTLIN_KEEP)
    loginAndFindRepos(githubApi, urls)
}

private fun loginAndFindRepos(githubApi: GithubApi, urls: List<String>): List<GithubRepo> {
    return urls.map {
        githubApi.getRepository(it)
    }
}
