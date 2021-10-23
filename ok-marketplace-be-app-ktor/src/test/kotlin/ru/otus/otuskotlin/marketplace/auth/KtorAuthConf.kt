package ru.otus.otuskotlin.marketplace.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import ru.otus.otuskotlin.marketplace.configs.KtorAuthConfig
import ru.otus.otuskotlin.marketplace.stubs.Bolt
import java.util.*

fun KtorAuthConfig.Companion.testUserToken(): String = testToken("TEST", "USER")
fun KtorAuthConfig.Companion.testModerToken(): String = testToken("TEST", "USER", "MODERATOR")
fun KtorAuthConfig.Companion.testAdminToken(): String = testToken("TEST", "USER", "ADMIN")
private fun testToken(vararg groups: String) = JWT.create()
    .withExpiresAt(
        GregorianCalendar().apply {
            set(2036, 0, 1, 0, 0, 0)
            timeZone = TimeZone.getTimeZone("UTC")
        }.time
    )
    .withAudience(KtorAuthConfig.TEST.audience)
    .withIssuer(KtorAuthConfig.TEST.issuer)
    .withClaim(KtorAuthConfig.ID_CLAIM, Bolt.owner.asString())
    .withClaim(KtorAuthConfig.FNAME_CLAIM, "Ivan")
    .withClaim(KtorAuthConfig.MNAME_CLAIM, "I.")
    .withClaim(KtorAuthConfig.LNAME_CLAIM, "Ivanov")
    .withArrayClaim(KtorAuthConfig.GROUPS_CLAIM, groups)
//            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
    .sign(Algorithm.HMAC256(KtorAuthConfig.TEST.secret))
    .apply { println("Test JWT token: $this") }

