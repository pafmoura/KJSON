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
    fun testFilter() {
        val obj = JsonObject(
            mapOf(
                "Professor" to JsonString("Paulo"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )

        val result = obj.filter(
            valuePredicate = { it -> it.data == "Paulo" },
            keyPredicate = { key -> key == "Alunos" }
        ).toJsonString()

        assertEquals("{\"Alunos\": [\"Paulo\"]}", result)

        val obj1 = JsonObject(
            mapOf(
                "Professor" to JsonString("Raimundo"),
                "extra" to obj,
            )
        )

        val result1 = obj1.filter(
            valuePredicate = { it -> it.data == "Paulo" },
            keyPredicate = { key -> key == "Alunos" }
        ).toJsonString()

        assertEquals("{\"extra\": {\"Alunos\": [\"Paulo\"]}}", result1)

    }

    @Test
    fun testMap() {
        val obj = JsonObject(
            mapOf(
                "Professor" to JsonString("Paulo"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )

        val result = obj.map(
            valueAction = { it -> JsonString(it.data.toString()) },
            keyAction = { it -> "-$it-" }
        ).toJsonString()

        assertEquals(
            "{\"-Professor-\": \"Paulo\", \"-Alunos-\": [\"Paulo\", \"Filipe\"], \"-Dias-\": [\"2\", \"5\", \"7\"]}",
            result
        )

        val obj1 = JsonObject(
            mapOf(
                "Professor" to JsonString("Raimundo"),
                "extra" to obj,
            )
        )

        val result1 = obj1.map(
            valueAction = { it ->  JsonString(it.data.toString()) },
            keyAction = { it -> "-$it-" }
        ).toJsonString()

        assertEquals("{\"-Professor-\": \"Raimundo\", \"-extra-\": {\"-Professor-\": \"Paulo\", \"-Alunos-\": [\"Paulo\", \"Filipe\"], \"-Dias-\": [\"2\", \"5\", \"7\"]}}", result1)
    }

    @Test
    fun testIsAllSameType() {
        val jsonObj1 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("João"),
                "Alunos" to JsonString("Paulo"),
                "Dias" to JsonString("Filipe")
            )
        )
        jsonObj1.isAllSameType()
    }

    @Test
    fun testGet() {
        val jsonObj1 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )

        val getval = jsonObj1.get("unidade curricular")
        assertEquals(JsonString("PA"), getval)

    }

    @Test
    fun testIsNotEmpty() {
        val jsonObj1 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )

        assertTrue(jsonObj1.isNotEmpty())
    }

    @Test
    fun testIsEmpty() {
        val jsonObj1 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )

        assertFalse(jsonObj1.isEmpty())
    }

    @Test
    fun testNumberOfProperties() {
        val jsonObj1 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )

        assertEquals(3, jsonObj1.numberOfProperties())
    }

    @Test
    fun testPut() {
        val jsonObj1 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )

        jsonObj1.put("unidade curricular", JsonString("ICO"))
        assertEquals(JsonString("ICO"), jsonObj1.get("unidade curricular"))

        jsonObj1.put("Alunos", JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))))
        assertEquals(
            JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
            jsonObj1.get("Alunos")
        )
    }

}
