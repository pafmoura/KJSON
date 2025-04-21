package iscte.tests.kjson

import iscte.main.kjson.JsonBoolean
import iscte.main.kjson.JsonNull
import iscte.main.kjson.JsonNumber
import iscte.main.kjson.JsonObject
import iscte.main.kjson.JsonString
import iscte.main.kjson.JsonValue
import iscte.main.kjson.MutableJsonObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

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
    fun testAcceptEntry() {
        val cadeiraJson: MutableJsonObject = getCadeiraMutableJson()
        var jsonPairs = ""
        cadeiraJson.accept { entry ->
            jsonPairs += "${entry.key} "
        }
        val jsonPairsString = "unidade curricular nota aprovado data de entrega professor nome idade homem gabinete "

        assertEquals(jsonPairsString, jsonPairs)
    }

    @Test
    fun isValidTest() {
        val cadeiraJson: MutableJsonObject = getCadeiraMutableJson()
        assertTrue(cadeiraJson.isValid())

        val notValidJson: MutableJsonObject = MutableJsonObject(
            mutableMapOf(
                "" to JsonString("PA"),
                "nota" to JsonNumber(20),
                "nota" to JsonNumber(16),
                "aprovado" to JsonBoolean(true),
            )
        )
        assertFalse(notValidJson.isValid())
    }


}