package ru.otus.otuskotlin.markeplace.springapp.controllers

import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.transport.mapping.kmp.*
import ru.otus.otuskotlin.marketplace.openapi.models.*
import java.time.Instant

@RestController
@RequestMapping("/ad")
class AdController(
    private val adService: AdService
) {

    @PostMapping("init")
    fun createAd(@RequestBody request: InitAdRequest): InitAdResponse {
        val context = MpContext(
            startTime = Instant.now()
        )
        return try {
            runBlocking {
                adService.initAd(context, request)
            }
        } catch (e: Throwable) {
            return adService.errorAd(context, e) as InitAdResponse
        }
    }

    @PostMapping("create")
    fun createAd(@RequestBody request: CreateAdRequest): CreateAdResponse {
        val context = MpContext(
            startTime = Instant.now()
        )
        return try {
            runBlocking {
                adService.createAd(context, request)
            }
        } catch (e: Throwable) {
            println("ERROR: $e")
            return adService.errorAd(context, e) as CreateAdResponse
        }
    }

    @PostMapping("read")
    fun createAd(@RequestBody request: ReadAdRequest): ReadAdResponse {
        val context = MpContext(
            startTime = Instant.now()
        )
        return try {
            runBlocking {
                adService.readAd(context, request)
            }
        } catch (e: Throwable) {
            return adService.errorAd(context, e) as ReadAdResponse
        }
    }

    @PostMapping("update")
    fun createAd(@RequestBody request: UpdateAdRequest): UpdateAdResponse {
        val context = MpContext(
            startTime = Instant.now()
        )
        return try {
            runBlocking {
                adService.updateAd(context, request)
            }
        } catch (e: Throwable) {
            return adService.errorAd(context, e) as UpdateAdResponse
        }
    }

    @PostMapping("delete")
    fun createAd(@RequestBody request: DeleteAdRequest): DeleteAdResponse {
        val context = MpContext(
            startTime = Instant.now()
        )
        return try {
            runBlocking {
                adService.deleteAd(context, request)
            }
        } catch (e: Throwable) {
            return adService.errorAd(context, e) as DeleteAdResponse
        }
    }

    @PostMapping("search")
    fun createAd(@RequestBody request: SearchAdRequest): SearchAdResponse {
        val context = MpContext(
            startTime = Instant.now()
        )
        return try {
            runBlocking {
                adService.searchAd(context, request)
            }
        } catch (e: Throwable) {
            return adService.errorAd(context, e) as SearchAdResponse
        }
    }

}
