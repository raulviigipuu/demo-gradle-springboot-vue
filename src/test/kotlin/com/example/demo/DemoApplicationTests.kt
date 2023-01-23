package com.example.demo

import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.open
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DemoApplicationTests {

    @Test
    fun mainViewTest() {
        open("http://localhost:8081")
        `$`("h1").shouldHave(text("This is a test assignment."))
    }

    @Test
    fun doctorListTest() {
        open("http://localhost:8081/doctors")
        Selenide.`$$`("table tbody tr").shouldHave(CollectionCondition.sizeGreaterThan(0))
    }

    @Test
    fun addDoctorTest() {
        open("http://localhost:8081/doctors")
        val sizeBefore = Selenide.`$$`("table tbody tr").size
        open("http://localhost:8081/doctors/add")
        `$`("input[id='firstName']").setValue("First")
        `$`("input[id='lastName']").setValue("Last")
        `$`("input[id='birthDate']").setValue("01/01/1970")
        `$`("button[type='submit']").click()
        open("http://localhost:8081/doctors")
        Selenide.`$$`("table tbody tr").shouldHave(CollectionCondition.sizeGreaterThan(sizeBefore))
    }

    @Test
    fun teamsListTest() {
        open("http://localhost:8081/teams")
        Selenide.`$$`("table tbody tr").shouldHave(CollectionCondition.sizeGreaterThan(0))
    }

    @Test
    fun addTeamTest() {
        open("http://localhost:8081/teams")
        val sizeBefore = Selenide.`$$`("table tbody tr").size
        open("http://localhost:8081/teams/add")
        `$`("input[id='name']").setValue("Team1")
        `$`("button[type='submit']").click()
        open("http://localhost:8081/teams")
        Selenide.`$$`("table tbody tr").shouldHave(CollectionCondition.sizeGreaterThan(sizeBefore))
    }
}
