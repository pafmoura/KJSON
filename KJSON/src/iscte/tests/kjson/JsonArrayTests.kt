//package iscte.tests.kjson
//
//import iscte.main.kjson.model.*
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.Assertions.*
//import javax.swing.DefaultListSelectionModel
//import kotlin.reflect.KClass
//
//class JsonArrayTests {
//
//    fun getCadeiraJson(): JsonObject {
//        return JsonObject(
//            mapOf(
//                "nota" to JsonNumber(20),
//                "unidade curricular" to JsonString("PA"),
//                "aprovado" to JsonBoolean(true),
//                "data de entrega" to JsonNull,
//                "professor" to JsonObject(
//                    mapOf(
//                        "nome" to JsonString("André"),
//                        "idade" to JsonNull,
//                        "homem" to JsonBoolean(true),
//                        "gabinete" to JsonString("D6.23")
//                    )
//                )
//            )
//        )
//    }
//
//    fun getCadeiraJson1(): JsonObject {
//        return JsonObject(
//            mutableMapOf(
//                "" to JsonString("PA"),
//                "nota" to JsonString("PA"),
//                "aprovado" to JsonString("PA"),
//            )
//        )
//    }
//
//    @Test
//    fun testAllSameType() {
//        val jsonArray = JsonArray(
//            listOf(
//                JsonString("Hello"),
//                JsonString("World"),
//                JsonString("!")
//            )
//        )
//        val visitor = VisitorAllSameType()
//        jsonArray.accept(visitor)
//        assertTrue(visitor.isValid())
//
//        assertTrue(jsonArray.isAllSameType())
//
//        val jsonArray1 = JsonArray(
//            listOf(
//                JsonString("Hello"),
//                JsonNumber(1),
//                JsonBoolean(false)
//            )
//        )
//        assertFalse(jsonArray1.isAllSameType())
//
//        val visitor1 = VisitorAllSameType()
//        jsonArray1.accept(visitor1)
//        assertFalse(visitor1.isValid())
//
//        val jsonArray2 = JsonArray(
//            listOf(
//                JsonString("Hello"),
//                getCadeiraJson(),
//                JsonString("World")
//            )
//        )
//
//        assertFalse(jsonArray2.isAllSameType())
//
//        val visitor2 = VisitorAllSameType()
//        jsonArray2.accept(visitor2)
//        assertFalse(visitor2.isValid())
//
//        val jsonArray3 = JsonArray(
//            listOf(
//                getCadeiraJson1(),
//                getCadeiraJson1(),
//                getCadeiraJson1()
//            )
//        )
//
//        assertTrue(jsonArray3.isAllSameType())
//
//        val visitor3 = VisitorAllSameType()
//        jsonArray3.accept(visitor3)
//        assertTrue(visitor3.isValid())
//
//        val jsonArray4 = JsonArray(
//            listOf(
//                getCadeiraJson1(),
//                JsonNull,
//                getCadeiraJson1()
//            )
//        )
//
//        assertFalse(jsonArray4.isAllSameType())
//
//        val visitor4 = VisitorAllSameType()
//        jsonArray4.accept(visitor4)
//        assertFalse(visitor4.isValid())
//
//        val jsonArray5 = JsonArray(
//            listOf(
//                JsonNull,
//                JsonNull,
//                JsonNull
//            )
//        )
//
//        assertFalse(jsonArray5.isAllSameType())
//
//        val visitor5 = VisitorAllSameType()
//        jsonArray5.accept(visitor5)
//        assertFalse(visitor5.isValid())
//    }
//
//
//    @Test
//    fun testJoinJsonArrays() {
//        val jsonArray1 = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//        val jsonArray2 = JsonArray(listOf(JsonNumber(4), JsonNumber(5), JsonNumber(6)))
//        jsonArray2.forEach { }
//        val expectedSum =
//            JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3), JsonNumber(4), JsonNumber(5), JsonNumber(6)))
//        val result = jsonArray1 + jsonArray2
//        assertEquals(expectedSum.toJsonString(), result.toJsonString())
//    }
//
//    @Test
//    fun testAddToJsonArrays() {
//        val jsonArray1 = MutableJsonArray(mutableListOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//        assertTrue { jsonArray1.isAllSameType() }
//        jsonArray1.add(JsonString("teste"))
//        assertFalse { jsonArray1.isAllSameType() }
//    }
//
//    @Test
//    fun testCustomVisitor() {
//        val cadeiraJson = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//        var firstElementClass: KClass<out JsonValue>? = null
//        var isValid = true
//
//        cadeiraJson.accept { element ->
//            if (firstElementClass == null)
//                firstElementClass = element::class
//
//            isValid = isValid && element::class == firstElementClass && element !is JsonNull
//        }
//
//        assertTrue(isValid)
//
//        val validJson = JsonArray(listOf(JsonString("1"), JsonNumber(2), JsonNumber(3)))
//
//        firstElementClass = null
//        isValid = true
//        validJson.accept { element ->
//            if (firstElementClass == null)
//                firstElementClass = element::class
//
//            isValid = isValid && element::class == firstElementClass && element !is JsonNull
//        }
//
//        assertFalse(isValid)
//    }
//
//    @Test
//    fun testSublist() {
//        var originalArray = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//        var newArray = originalArray.subList(1, 3)
//        assertEquals(JsonArray(listOf(JsonNumber(2), JsonNumber(3))).toJsonString(), newArray.toJsonString())
//    }
//
//    @Test
//    fun testArrayEquals() {
//        var originalArray = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//        var newArray = originalArray.subList(1, 3)
//        assertTrue(JsonArray(listOf(JsonNumber(2), JsonNumber(3))).equals(newArray))
//    }
//
//    @Test
//    fun testMapArray() {
//        var arr = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//        var newArr = arr.map({ x -> x as JsonNumber + JsonNumber(1) })
//        assertEquals(JsonArray(listOf(JsonNumber(2), JsonNumber(3), JsonNumber(4))), newArr)
//
//        var arr1 = JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//        var newArr1 = arr1.map { x ->
//            when (x) {
//                is JsonNumber -> x + JsonNumber(1)
//                else -> x
//            }
//        }
//
//        assertEquals(JsonArray(listOf(JsonNumber(2), JsonNumber(3), JsonNumber(4))), newArr1)
//
//
//        var arr2 =
//            JsonArray(
//                listOf(
//                    JsonNumber(1),
//                    JsonNumber(2),
//                    JsonArray(listOf(JsonNumber(1), JsonNumber(2), JsonNumber(3)))
//                )
//            )
//        var newArr2 = arr2.map { x ->
//            when (x) {
//                is JsonNumber -> x + JsonNumber(1)
//                else -> x
//            }
//
//        }
//        assertEquals(
//            JsonArray(
//                listOf(
//                    JsonNumber(2),
//                    JsonNumber(3),
//                    JsonArray(listOf(JsonNumber(2), JsonNumber(3), JsonNumber(4)))
//                )
//            ), newArr2
//        )
//
//
//
//        var arr3 = JsonArray(
//            listOf(
//                JsonString("Lista de Alunos"),
//                JsonObject(
//                    mapOf(
//                        "nome" to JsonString("Adérito"),
//                        "unidade curricular" to JsonString("PA"),
//                        "aprovado" to JsonString("Confirmado")
//                    )
//                )
//            )
//        )
//
//        var newArr3 = arr3.map(
//            { x ->
//                when (x) {
//                    is JsonString -> JsonString("-${x.data}-")
//                    else -> x
//                }
//            },
//            keyAction = { y ->
//                "---${y}---"
//            }
//
//
//        )
//
//        assertEquals(
//            "[\"-Lista de Alunos-\", {\"---nome---\": \"-Adérito-\", \"---unidade curricular---\": \"-PA-\", \"---aprovado---\": \"-Confirmado-\"}]",
//            newArr3.toJsonString()
//        )
//    }
//
//@Test
//fun testFilterArray() {
//    val jsonArray = JsonArray(
//        listOf(
//            MutableJsonObject(
//                mutableMapOf(
//                    "nome" to JsonString("Adérito"),
//                    "unidade curricular" to JsonString("PA"),
//                    "idade" to JsonNumber(23)
//                )
//            ),
//            MutableJsonObject(
//                mutableMapOf(
//                    "nome" to JsonString("André"),
//                    "unidade curricular" to JsonString("PA"),
//                    "idade" to JsonNumber(26)
//                )
//            ),
//            JsonArray(
//                listOf(
//                    JsonNumber(47),
//                    JsonNumber(42),
//                    JsonBoolean(true)
//                )
//
//            )
//    ))
//    val filterByAge = jsonArray.filter(
//        predicate = { v -> v is JsonNumber && v.data as Int > 25 },
//      // keyPredicate = { k -> k == "idade" }
//    )
//    print(filterByAge.toJsonString())
//
//    var jarr2 = JsonArray(
//        listOf(
//            JsonNumber(47),
//            JsonNumber(42),
//            JsonNumber(12),
//            JsonNumber(27)
//        ))
//
//   // var filterOver25 = jarr2.filter(
//     //   predicate = { v -> v is JsonNumber && v.data as Int > 25 })
//
//}
//
//}
//
//
//
//
//
//
//
