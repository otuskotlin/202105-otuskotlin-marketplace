package ru.otus.otuskotlin.marketplace.auth

import io.ktor.http.*
import org.junit.Test
import ru.otus.otuskotlin.marketplace.Utils
import ru.otus.otuskotlin.marketplace.configs.AppKtorConfig
import ru.otus.otuskotlin.marketplace.configs.KtorAuthConfig
import ru.otus.otuskotlin.marketplace.controllers.RouterTest
import ru.otus.otuskotlin.marketplace.openapi.models.ReadAdRequest
import ru.otus.otuskotlin.marketplace.openapi.models.ReadAdResponse
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class AuthWrongIssuer : RouterTest() {
    @Test
    fun authPositiveTest() {
        val data = ReadAdRequest(readAdId = "99999", debug = Utils.stubSuccessDebug)

        testPostRequest<ReadAdResponse>(
            body=data,
            uri="/ad/read",
            config = AppKtorConfig(
                auth = KtorAuthConfig.TEST.copy()
            )
        ) {
            assertEquals(ReadAdResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponseAd.copy(id = "99999"), readAd)
        }
    }
    @Test
    fun authWrongIssuerTest() {
        val data = ReadAdRequest(readAdId = "99999", debug = Utils.stubSuccessDebug)

        testPostRequest<ReadAdResponse>(
            body=data,
            uri="/ad/read",
            config = AppKtorConfig(
                auth = KtorAuthConfig.TEST.copy(
                    issuer = "some other company"
                )
            ),
            result = HttpStatusCode.Unauthorized,
        )
    }
    @Test
    fun authWrongSecretTest() {
        val data = ReadAdRequest(readAdId = "99999", debug = Utils.stubSuccessDebug)

        testPostRequest<ReadAdResponse>(
            body=data,
            uri="/ad/read",
            config = AppKtorConfig(
                auth = KtorAuthConfig.TEST.copy(
                    secret = "wrong secret"
                )
            ),
            result = HttpStatusCode.Unauthorized,
        )
    }

}
