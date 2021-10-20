package ru.otus.otuskotlin.marketplace.configs

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import java.util.*

data class KtorAuthConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
) {
    constructor(environment: ApplicationEnvironment): this(
        secret = environment.config.property("jwt.secret").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        realm = environment.config.property("jwt.realm").getString()
    )

    companion object {
        const val ID_CLAIM = "id"
        const val GROUPS_CLAIM = "groups"

        val TEST = KtorAuthConfig(
            secret = "secret",
            issuer = "OtusKotlin",
            audience = "ad-users",
            realm = "Access to Ads"
        )
        fun testToken(): String = JWT.create()
            .withExpiresAt(
                GregorianCalendar().apply {
                    set(2035,1,1,0,0,0)
                }.time
            )
            .withAudience(TEST.audience)
            .withIssuer(TEST.issuer)
            .withClaim(ID_CLAIM, "00000000-0000-0000-0000-000000000001")
            .withArrayClaim(GROUPS_CLAIM, arrayOf("TEST"))
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(TEST.secret))
            .apply { println("Test JWT token: $this") }
    }
}
