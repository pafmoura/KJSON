package iscte.tests.kjson

import iscte.main.kjson.model.*
import iscte.main.kjson.utils.JsonReflection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class JsonReflectionTests {

    data class Course(
        val name: String,
        val credits: Int,
        val evaluation: List<EvalItem>
    )


    data class EvalItem(
        val name: String,
        val percentage: Double,
        val mandatory: Boolean,
        val type: EvalType?
    ) {

    }


    enum class EvalType {
        TEST, PROJECT, EXAM
    }

    val course = Course(
        "PA", 6, listOf(
            EvalItem("quizzes", .2, false, null),
            EvalItem("project", .8, true, EvalType.PROJECT)
        )
    )

    val courseJson = JsonObject(
        mapOf(
            "name" to JsonString("PA"),
            "credits" to JsonNumber(6),
            "evaluation" to JsonArray(
                listOf(
                    JsonObject(
                        mapOf(
                            "name" to JsonString("quizzes"),
                            "percentage" to JsonNumber(0.2),
                            "mandatory" to JsonBoolean(false),
                            "type" to JsonNull
                        )
                    ),
                    JsonObject(
                        mapOf(
                            "name" to JsonString("project"),
                            "percentage" to JsonNumber(0.8),
                            "mandatory" to JsonBoolean(true),
                            "type" to JsonString("PROJECT")
                        )
                    )
                )
            )
        )
    )

    @Test
    fun testProjectStatementCase() {
        val expected =
            "{\"name\": \"PA\", \"credits\": 6, \"evaluation\": [{\"name\": \"quizzes\", \"percentage\": 0.2, \"mandatory\": false, \"type\": null},{\"name\": \"project\", \"percentage\": 0.8, \"mandatory\": true, \"type\": \"PROJECT\"}]}"
        assertEquals(expected, JsonReflection.toJsonValue(course).toJsonString())

    }

    @Test
    fun testListReflection() {
        val list = listOf(course, course)

        val expectedObj = JsonArray(
            listOf(
                courseJson, courseJson
            )
        )


        assertEquals(expectedObj, JsonReflection.toJsonValue(list))
    }

    @Test
    fun testMapReflection() {
        val map = mapOf(
            "course" to course,
            "course2" to course
        )

        val expectedObj = JsonObject(
            mapOf(
                "course" to courseJson,
                "course2" to courseJson
            )
        )
        assertEquals(expectedObj, JsonReflection.toJsonValue(map))
    }

    @Test
    fun testReflectionThrowsIllegalKey(){
        val map = mapOf(
            1 to course,
            "course2" to course
        )

        try {
            JsonReflection.toJsonValue(map)
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message == "Key must be string")
        }
    }

    @Test
    fun testThrowsNotDataClass(){
        class NotDataClass(
            val name: String,
            val credits: Int
        )
        val notDataClass = NotDataClass("PA", 6)
        try {
            JsonReflection.toJsonValue(notDataClass)
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message == "Value must be a data class")
        }
    }




}