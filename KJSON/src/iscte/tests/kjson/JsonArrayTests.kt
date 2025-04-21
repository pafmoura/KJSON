package iscte.tests.kjson

import iscte.main.kjson.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class JsonArrayTests {

    @Test
    fun allSameTypeTest() {
        val jsonArray = JsonArray(
            listOf(
                JsonString("Hello"),
                JsonString("World"),
                JsonString("!")
            )
        )
        assertTrue(jsonArray.allSameType())

        val jsonArray2 = JsonArray(
            listOf(
                JsonString("Hello"),
                JsonNumber(1),
                JsonBoolean(false)
            )
        )
        assertFalse(jsonArray2.allSameType())
    }
}