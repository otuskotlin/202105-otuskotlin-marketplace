package ru.otus.otuskotlin.markeplace.springapp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import ru.otus.otuskotlin.marketplace.openapi.models.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun createRequest() {
        val requestId = "123"
        val request = CreateAdRequest(
            requestId = requestId,
            createAd = CreateableAd(
                title = "One",
                description = "Two",
                ownerId = "345",
                visibility = AdVisibility.OWNER_ONLY,
                dealSide = AdDealSide.DEMAND,
                product = AdProductBolt(
                    lengh = 30.0,
                    diameter = 8.0,
                    threadPitch = 0.4,
                )
            ),
            debug = BaseDebugRequest(
                mode = BaseDebugRequest.Mode.STUB,
                stubCase = BaseDebugRequest.StubCase.SUCCESS
            )
        )
        val response: CreateAdResponse = restTemplate.postForObject(
            "http://localhost:${port}/ad/create",
            request,
            CreateAdResponse::class.java,
        )
        assertThat(response.requestId).isEqualTo(requestId)
        assertThat(response.createdAd?.visibility).isEqualTo(AdVisibility.OWNER_ONLY)
    }

}
