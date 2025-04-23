package iscte.tests.kjson

import iscte.main.kjson.model.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.reflect.KClass

class JsonObjectTests {

    fun getCadeiraJson(): JsonObject {
        return JsonObject(
            mapOf(
                "unidade curricular" to JsonString("PA"),
                "nota" to JsonNumber(20),
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

    fun getCadeiraMutableJson(): MutableJsonObject {
        return MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "nota" to JsonNumber(20),
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

    @Test
    fun testToJsonString() {
        val cadeiraJson: JsonObject = getCadeiraJson()

        val professorJsonString = "{\"nome\": \"André\", \"idade\": null, \"homem\": true, \"gabinete\": \"D6.23\"}"
        val cadeiraJsonString =
            "{\"unidade curricular\": \"PA\", \"nota\": 20, \"aprovado\": true, \"data de entrega\": null, \"professor\": $professorJsonString}"

        assertEquals(cadeiraJsonString, cadeiraJson.toJsonString())
    }

    @Test
    fun testMutableToJsonString() {
        val cadeiraJson: MutableJsonObject = getCadeiraMutableJson()

        val professorJsonString = "{\"nome\": \"André\", \"idade\": null, \"homem\": true, \"gabinete\": \"D6.23\"}"
        val cadeiraJsonString =
            "{\"unidade curricular\": \"PA\", \"nota\": 20, \"aprovado\": true, \"data de entrega\": null, \"professor\": $professorJsonString}"

        assertEquals(cadeiraJsonString, cadeiraJson.toJsonString())
    }

    @Test
    fun testMutableOperations() {
        val cadeiraJson: MutableJsonObject = getCadeiraMutableJson()
        cadeiraJson.put("ano", JsonNumber(2025))
        val ano = cadeiraJson["ano"]!!
        val anoRemoved = cadeiraJson.remove("ano")!!

        val professorJsonString = "{\"nome\": \"André\", \"idade\": null, \"homem\": true, \"gabinete\": \"D6.23\"}"
        val cadeiraJsonString =
            "{\"unidade curricular\": \"PA\", \"nota\": 20, \"aprovado\": true, \"data de entrega\": null, \"professor\": $professorJsonString}"

        assertEquals(cadeiraJsonString, cadeiraJson.toJsonString())
        assertEquals(2025, ano.data)
        assertEquals(2025, anoRemoved.data)
    }

    @Test
    fun testVisitorValidObject() {
        val cadeiraJson: MutableJsonObject = getCadeiraMutableJson()
        val visitor = VisitorValidObject()

        cadeiraJson.accept(visitor)

        assertTrue(visitor.isValid())

        assertTrue(cadeiraJson.isValidObject())

        val notValidJson: MutableJsonObject = MutableJsonObject(
            mutableMapOf(
                "" to JsonString("PA"),
                "nota" to JsonNumber(20),
                "nota" to JsonNumber(16),
                "aprovado" to JsonBoolean(true),
            )
        )

        assertTrue(notValidJson.isValidObject())
    }

    @Test
    fun testVisitorAllMatch() {
        val cadeiraJson: MutableJsonObject = getCadeiraMutableJson()
        val visitor = VisitorAllSameType()

        cadeiraJson.accept(visitor)

        assertFalse(visitor.isValid())

        val validJson: MutableJsonObject = MutableJsonObject(
            mutableMapOf(
                "" to JsonString("PA"),
                "nota" to JsonString("PA"),
                "nota" to JsonString("PA"),
                "aprovado" to JsonString("PA"),
            )
        )

        val visitor1 = VisitorAllSameType()

        validJson.accept(visitor1)

        assertTrue(visitor1.isValid())
    }


    @Test
    fun testSerialization() {

        val jsonArray1 = JsonArray(listOf(JsonNumber(2025), JsonNumber(12)))
        val expectedArray1 = "[2025, 12]"
        assertEquals(expectedArray1, jsonArray1.toJsonString())


        val jsonObject1 = JsonObject(
            mapOf(
                "name" to JsonString("John"),
                "age" to JsonNumber(30),
                "isStudent" to JsonBoolean(false),
                "courses" to JsonNull
            )
        )

        val expectedJsonString = "{\"name\": \"John\", \"age\": 30, \"isStudent\": false, \"courses\": null}"
        assertEquals(expectedJsonString, jsonObject1.toJsonString())
    }

    @Test
    fun testCustomVisitorAllTypes() {
        val cadeiraJson: MutableJsonObject = getCadeiraMutableJson()
        var firstElementClass: KClass<out JsonValue>? = null
        var isValid = true

        cadeiraJson.accept{ entry ->
            if (firstElementClass == null)
                firstElementClass = entry.value::class

            isValid = isValid && entry.value::class == firstElementClass && entry.value !is JsonNull
        }

        assertFalse(isValid)

        val validJson: MutableJsonObject = MutableJsonObject(
            mutableMapOf(
                "" to JsonString("PA"),
                "nota" to JsonString("PA"),
                "nota" to JsonString("PA"),
                "aprovado" to JsonString("PA"),
            )
        )

        firstElementClass = null
        isValid = true
        validJson.accept{ entry ->
            if (firstElementClass == null)
                firstElementClass = entry.value::class

            isValid = isValid && entry.value::class == firstElementClass && entry.value !is JsonNull
        }

        assertTrue(isValid)
    }

    @Test
    fun testCustomVisitorValidObject(){
        val cadeiraJson: MutableJsonObject = getCadeiraMutableJson()
        var keys = mutableSetOf<String>()
        var isValid = true

        cadeiraJson.accept{ entry ->
            isValid = isValid && keys.add(entry.key)
        }

        assertTrue(isValid)

        val notValidJson: MutableJsonObject = MutableJsonObject(
            mutableMapOf(
                "" to JsonString("PA"),
                "nota" to JsonNumber(20),
                "nota" to JsonNumber(16),
                "aprovado" to JsonBoolean(true),
            )
        )

        keys = mutableSetOf<String>()
        isValid = true

        notValidJson.accept{ entry ->
            isValid = isValid && keys.add(entry.key)
        }

        assertTrue(isValid)
    }

}