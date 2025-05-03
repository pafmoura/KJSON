package iscte.tests.web

import iscte.main.kjson.model.JsonString
import iscte.main.web.GetJson
import org.junit.Assert.assertThrows
import org.junit.Test

class GetJsonTests {


    @Test
    fun testInvalidClasses() {
        assertThrows(IllegalArgumentException::class.java) {
            val app = GetJson(JsonString::class)
        }
        assertThrows(IllegalArgumentException::class.java) {
            val app = GetJson(Controller::class, JsonString::class)
        }
    }

    @Test
    fun testValidClasses() {
        GetJson(Controller::class)
        GetJson(Controller::class, Controller::class)
    }


}