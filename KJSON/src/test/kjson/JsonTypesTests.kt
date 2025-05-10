package kjson

import kjson.model.JsonBoolean
import kjson.model.JsonNull
import kjson.model.JsonNumber
import kjson.model.JsonString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


class JsonTypesTests {

    @Test
    fun createJsonNumbersTest() {
        assertEquals(1, JsonNumber(1).data)
        assertEquals(2.5, JsonNumber(2.5).data)
    }

    @Test
    fun createJsonStringsTest() {
        assertEquals("Hello", JsonString("Hello").data)
        assertEquals("World", JsonString("World").data)
    }

    @Test
    fun createJsonBooleansTest() {
        assertTrue(JsonBoolean(true).data)
        assertFalse(JsonBoolean(false).data)
    }

    @Test
    fun createJsonNullTest() {
        assertNull(JsonNull.data)
    }

    @Test
    fun sumJsonNumbersTest() {
        val num1 = JsonNumber(5)
        val num2 = JsonNumber(10)
        val result = num1 + num2
        assertEquals(15, result.data)
        assertTrue(result.data is Int)

        val num1long = JsonNumber(5L)
        val num2long = JsonNumber(10L)
        val resultlong = num1long + num2long
        assertEquals(15L, resultlong.data)
        assertTrue(resultlong.data is Long)

        val num1double = JsonNumber(5.0)
        val num2double = JsonNumber(10.0)
        val resultdouble = num1double + num2double
        assertEquals(15.0, resultdouble.data)
        assertTrue(resultdouble.data is Double)

        val num1mixed = JsonNumber(5)
        val num2mixed = JsonNumber(10.0)
        val resultmixed = num1mixed + num2mixed
        assertEquals(15.0, resultmixed.data)
        assertTrue(resultmixed.data is Double)


        val num1pos = JsonNumber(2.0)
        val num2neg = JsonNumber(-1.0)
        val resultposneg = num1pos + num2neg
        assertEquals(1.0, resultposneg.data)
        assertTrue(resultposneg.data is Double)


    }

    @Test
    fun stringOperatorsTest() {
        val str1 = JsonString("Hello")
        val str2 = JsonString("World")
        val result = str1 + str2
        assertEquals("HelloWorld", result.data)

        val num1 = JsonNumber(5)
        val resultNum = str1 + num1
        assertEquals("Hello5", resultNum.data)

        val bool1 = JsonBoolean(true)
        val resultBool = str1 + bool1
        assertEquals("Hellotrue", resultBool.data)

        val nullValue = JsonNull
        val resultNull = str1 + nullValue
        assertEquals("Hellonull", resultNull.data)

        val stringEquals1 = JsonString("Hello")
        val stringEquals2 = JsonString("Hello")
        val stringEquals3 = JsonString("World")
        assertTrue(stringEquals1 == stringEquals2)
        assertFalse(stringEquals1 == stringEquals3)
    }




}