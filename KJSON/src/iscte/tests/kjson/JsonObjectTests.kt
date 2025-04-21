package iscte.tests.kjson

import iscte.main.kjson.model.JsonBoolean
import iscte.main.kjson.model.JsonNull
import iscte.main.kjson.model.JsonNumber
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonString
import iscte.main.kjson.model.MutableJsonObject
import iscte.main.kjson.model.VisitorAllSameType
import iscte.main.kjson.model.VisitorValidObject
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

        assertFalse(notValidJson.isValidObject())
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

        cadeiraJson.accept(visitor)

        assertTrue(visitor1.isValid())
    }
}