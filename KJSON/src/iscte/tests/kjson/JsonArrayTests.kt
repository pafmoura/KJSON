import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonNumber
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonString
import iscte.main.kjson.model.*
import iscte.main.kjson.utils.JsonReflection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class JsonArrayTests {

    fun getJsonArray(): JsonArray {
        return JsonArray(
            listOf(
                JsonString("Lista de Candidatos"),
                JsonNumber(2025),
                JsonObject(
                    mapOf(
                        "name" to JsonString("João"),
                        "age" to JsonNumber(27),
                        "city" to JsonString("Lisboa")
                    )
                ),
                JsonObject(
                    mapOf(
                        "name" to JsonString("Maria"),
                        "age" to JsonNumber(25),
                        "city" to JsonString("Porto")
                    )
                ),

                )
        )

    }

    @Test
    fun testArrayToString() {
        val jsonArray = getJsonArray()
        val expected = "[\"Lista de Candidatos\",2025,{\"name\": \"João\", \"age\": 27, \"city\": \"Lisboa\"},{\"name\": \"Maria\", \"age\": 25, \"city\": \"Porto\"}]"
        assertEquals(expected, jsonArray.toJsonString())
    }

    @Test
    fun testArrayFilter() {
        val jsonObj1 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )
        val jsonObj2 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Pedro"), JsonString("Paulo"))),
                "Dias" to JsonArray(listOf(JsonNumber(3), JsonNumber(6), JsonNumber(9)))
            )
        )
        val arr = JsonArray(listOf(JsonString("Paulo"), jsonObj1, jsonObj2))

        val result =
            arr.filter(
                valuePredicate = { it -> it.data == "Paulo" },
                keyPredicate = { key -> key == "Alunos" }
            ).toJsonString()

        assertEquals("[\"Paulo\",{\"Alunos\": [\"Paulo\"]},{\"Alunos\": [\"Paulo\"]}]", result)

        val arr1 = JsonArray(listOf(JsonString("Paulo"), jsonObj1, jsonObj2, arr))

        val result1 =
            arr1.filter(
                valuePredicate = { it -> it.data == "Paulo" },
                keyPredicate = { key -> key == "Alunos" }
            ).toJsonString()
        assertEquals("[\"Paulo\",{\"Alunos\": [\"Paulo\"]},{\"Alunos\": [\"Paulo\"]},[\"Paulo\",{\"Alunos\": [\"Paulo\"]},{\"Alunos\": [\"Paulo\"]}]]", result1)
    }

    @Test
    fun testArrayMap() {
        val jsonObj1 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
                "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
            )
        )
        val jsonObj2 = MutableJsonObject(
            mutableMapOf(
                "unidade curricular" to JsonString("PA"),
                "Alunos" to JsonArray(listOf(JsonString("Pedro"), JsonString("Paulo"))),
                "Dias" to JsonArray(listOf(JsonNumber(3), JsonNumber(6), JsonNumber(9)))
            )
        )
        val arr = JsonArray(listOf(JsonString("Paulo"), jsonObj1, jsonObj2))

        val result =
            arr.map(
                valueAction = { it -> JsonString(it.data.toString()) },
                keyAction = { it -> "-$it-" }
            ).toJsonString()


        assertEquals(
            "[\"Paulo\",{\"-unidade curricular-\": \"PA\", \"-Alunos-\": [\"Paulo\",\"Filipe\"], \"-Dias-\": [\"2\",\"5\",\"7\"]},{\"-unidade curricular-\": \"PA\", \"-Alunos-\": [\"Pedro\",\"Paulo\"], \"-Dias-\": [\"3\",\"6\",\"9\"]}]",
            result
        )

        val arr1 = JsonArray(listOf(JsonString("Paulo"), jsonObj1, jsonObj2, arr))

        val result1 =
            arr1.map(
                valueAction = { it -> if (it.data == "Paulo") JsonNumber(1) else it },
                keyAction = { key -> "-$key-"}
            ).toJsonString()

        assertEquals("[1,{\"-unidade curricular-\": \"PA\", \"-Alunos-\": [1,\"Filipe\"], \"-Dias-\": [2,5,7]},{\"-unidade curricular-\": \"PA\", \"-Alunos-\": [\"Pedro\",1], \"-Dias-\": [3,6,9]},[1,{\"-unidade curricular-\": \"PA\", \"-Alunos-\": [1,\"Filipe\"], \"-Dias-\": [2,5,7]},{\"-unidade curricular-\": \"PA\", \"-Alunos-\": [\"Pedro\",1], \"-Dias-\": [3,6,9]}]]", result1)
    }

    @Test
    fun isAllSameType() {
        val jsonArray = JsonArray(
            listOf(
                JsonNumber(10), JsonNumber(53), JsonString("Paulo")
            )
        )
        assertFalse(jsonArray.isAllSameType())

        val jsonArray2 = JsonArray(
            listOf(
                JsonNumber(10), JsonNumber(53), JsonNumber(20), JsonArray(
                    listOf(JsonNumber(5), JsonNumber(7))
                )
            )
        )
        assertFalse(jsonArray2.isAllSameType())

        val jsonArray3 = JsonArray(
            listOf(
                JsonObject(
                    mapOf(
                        "name" to JsonString("João"),
                    )
                ),
                JsonObject(
                    mapOf(
                        "name" to JsonString("Maria"),
                    )
                )
            )
        )
        assertTrue(jsonArray3.isAllSameType())

    }

    @Test
    fun testIsEmpty() {
        val jsonArray = JsonArray(listOf())
        assertTrue(jsonArray.isEmpty())
    }

    @Test
    fun testIsNotEmpty() {
        val jsonArray = JsonArray(listOf(JsonString("test")))
        assertTrue(jsonArray.isNotEmpty())
    }

    @Test
    fun testGet() {
        val jsonArray = JsonArray(listOf(JsonString("test"), JsonNumber(123)))
        assertEquals(JsonString("test"), jsonArray.get(0))
        assertEquals(JsonNumber(123), jsonArray.get(1))
    }

    @Test
    fun testNumberOfProperties() {
        val jsonArray = JsonArray(listOf(JsonString("test"), JsonNumber(123)))
        assertEquals(2, jsonArray.numberOfProperties())
    }

    @Test
    fun testSubList() {
        val jsonArray = JsonArray(listOf(JsonString("test"), JsonNumber(123), JsonString("test2")))
        val subList = jsonArray.subList(0, 2)
        assertEquals(JsonArray(listOf(JsonString("test"), JsonNumber(123))), subList)
    }

    @Test
    fun testPlus() {
        val jsonArray1 = JsonArray(listOf(JsonString("test"), JsonNumber(123)))
        val jsonArray2 = JsonArray(listOf(JsonString("test2"), JsonNumber(456)))
        val result = jsonArray1 + jsonArray2
        assertEquals(
            JsonArray(listOf(JsonString("test"), JsonNumber(123), JsonString("test2"), JsonNumber(456))),
            result
        )
    }

    @Test
    fun testEquals() {
        val jsonArray1 = JsonArray(listOf(JsonString("test"), JsonNumber(123)))
        val jsonArray2 = JsonArray(listOf(JsonString("test"), JsonNumber(123)))
        assertTrue(jsonArray1 == jsonArray2)
    }

    @Test
    fun testHashCode() {
        val jsonArray = JsonArray(listOf(JsonString("test"), JsonNumber(123)))
        assertEquals(jsonArray.hashCode(), jsonArray.hashCode())
    }

    @Test
    fun testAdd() {
        val jsonArray = MutableJsonArray(mutableListOf(JsonString("paulo")))
        jsonArray.add(JsonString("filipe"))
        assertEquals(JsonArray(listOf(JsonString("paulo"), JsonString("filipe"))), jsonArray)
    }

    @Test
    fun testRemoveAt() {
        val jsonArray = MutableJsonArray(mutableListOf(JsonString("paulo"), JsonString("filipe")))
        jsonArray.removeAt(0)
        assertEquals(JsonArray(listOf(JsonString("filipe"))), jsonArray)
    }

    @Test
    fun clear() {
        val jsonArray = MutableJsonArray(mutableListOf(JsonString("paulo"), JsonString("filipe")))
        jsonArray.clear()
        assertEquals(JsonArray(listOf()), jsonArray)
    }


}



