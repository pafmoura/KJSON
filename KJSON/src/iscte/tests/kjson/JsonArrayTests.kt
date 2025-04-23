package iscte.tests.kjson

import iscte.main.kjson.model.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.reflect.KClass

class JsonArrayTests {

    fun getCadeiraJson(): JsonObject {
        return JsonObject(
            mapOf(
                "nota" to JsonNumber(20),
                "unidade curricular" to JsonString("PA"),
                "aprovado" to JsonBoolean(true),
                "data de entrega" to JsonNull,
                "professor" to JsonObject(
                    mapOf(
                        "nome" to JsonString("André"),
                        "idade" to JsonNull,
                        "homem" to JsonBoolean(true),
                        "gabinete" to JsonString("D6.23")
                    )
                )
            )
        )
    }

    fun getCadeiraJson1(): JsonObject {
        return JsonObject(
            mutableMapOf(
                "" to JsonString("PA"),
                "nota" to JsonString("PA"),
                "aprovado" to JsonString("PA"),
            )
        )
    }

    @Test
    fun testAllSameType() {
        val jsonArray = JsonArray(
            listOf(
                JsonString("Hello"),
                JsonString("World"),
                JsonString("!")
            )
        )
        val visitor = VisitorAllSameType()
        jsonArray.accept(visitor)
        assertTrue(visitor.isValid())

        assertTrue(jsonArray.isAllSameType())

        val jsonArray1 = JsonArray(
            listOf(
                JsonString("Hello"),
                JsonNumber(1),
                JsonBoolean(false)
            )
        )
        assertFalse(jsonArray1.isAllSameType())

        val visitor1 = VisitorAllSameType()
        jsonArray1.accept(visitor1)
        assertFalse(visitor1.isValid())

        val jsonArray2 = JsonArray(
            listOf(
                JsonString("Hello"),
                getCadeiraJson(),
                JsonString("World")
            )
        )

        assertFalse(jsonArray2.isAllSameType())

        val visitor2 = VisitorAllSameType()
        jsonArray2.accept(visitor2)
        assertFalse(visitor2.isValid())

        val jsonArray3 = JsonArray(
            listOf(
                getCadeiraJson1(),
                getCadeiraJson1(),
                getCadeiraJson1()
            )
        )

        assertTrue(jsonArray3.isAllSameType())

        val visitor3 = VisitorAllSameType()
        jsonArray3.accept(visitor3)
        assertTrue(visitor3.isValid())

        val jsonArray4 = JsonArray(
            listOf(
                getCadeiraJson1(),
                JsonNull,
                getCadeiraJson1()
            )
        )

        assertFalse(jsonArray4.isAllSameType())

        val visitor4 = VisitorAllSameType()
        jsonArray4.accept(visitor4)
        assertFalse(visitor4.isValid())

        val jsonArray5 = JsonArray(
            listOf(
                JsonNull,
                JsonNull,
                JsonNull
            )
        )

        assertFalse(jsonArray5.isAllSameType())

        val visitor5 = VisitorAllSameType()
        jsonArray5.accept(visitor5)
        assertFalse(visitor5.isValid())
    }


    @Test
    fun testJoinJsonArrays() {
        val jsonArray1 = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
        val jsonArray2 = JsonArray(listOf(JsonNumber(4), JsonNumber(5), JsonNumber(6)))
        jsonArray2.forEach {  }
        val expectedSum =
            JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3), JsonNumber(4), JsonNumber(5), JsonNumber(6)))
        val result = jsonArray1 + jsonArray2
        assertEquals(expectedSum.toJsonString(), result.toJsonString())
    }

    @Test
    fun testAddToJsonArrays() {
        val jsonArray1 = MutableJsonArray(mutableListOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//FINISH

    }

    @Test
    fun testCustomVisitor() {
        val cadeiraJson = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
        var firstElementClass: KClass<out JsonValue>? = null
        var isValid = true

        cadeiraJson.accept{ element ->
            if (firstElementClass == null)
                firstElementClass = element::class

            isValid = isValid && element::class == firstElementClass && element !is JsonNull
        }

        assertTrue(isValid)

        val validJson = JsonArray(listOf(JsonString("1"), JsonNumber(2), JsonNumber(3)))

        firstElementClass = null
        isValid = true
        validJson.accept{ element ->
            if (firstElementClass == null)
                firstElementClass = element::class

            isValid = isValid && element::class == firstElementClass && element !is JsonNull
        }

        assertFalse(isValid)
    }

}