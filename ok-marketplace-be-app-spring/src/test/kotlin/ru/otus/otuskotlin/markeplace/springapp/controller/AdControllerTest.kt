package ru.otus.otuskotlin.markeplace.springapp.controller

import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import ru.otus.otuskotlin.markeplace.springapp.service.AdService

@WebMvcTest(controllers = [AdController::class])
internal class AdControllerTest {

    @MockkBean
    private lateinit var adService: AdService

    @Autowired
    private lateinit var mockMvc: MockMvc

    fun test() {
        mockMvc.perform {

        }
    }

}