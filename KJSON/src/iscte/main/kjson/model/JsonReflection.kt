package iscte.main.kjson.utils

import iscte.main.kjson.model.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * Utility object for converting Kotlin objects to JSON values.
 *
 * This object provides a method to convert various types of Kotlin objects, including data classes,
 * enums, lists, and maps, into their corresponding JSON representations.
 */
object JsonReflection {

    /**
     * Converts a Kotlin object to a JSON value.
     *
     * This method takes an object of any type and converts it to a [JsonValue].
     * It supports null values, primitive types, strings, enums, lists, maps,
     * and data classes.
     *
     * @param value The object to convert to JSON.
     * @return A [JsonValue] representing the object.
     *
     * @throws IllegalArgumentException If the object is of an unsupported type.
     *
     * This method uses reflection to inspect the properties of data classes
     */
    fun toJsonValue(value: Any?): JsonValue {
        return when (value) {
            null -> JsonNull
            is Boolean -> JsonBoolean(value)
            is Number -> JsonNumber(value)
            is String -> JsonString(value)
            is Enum<*> -> JsonString(value.name)
            is List<*> -> JsonArray(value.map { toJsonValue(it) })
            is Map<*, *> -> {
                if (value.keys.all { it is String }) {
                    JsonObject(value.entries.associate { (k, v) ->
                        k as String to toJsonValue(v)
                    })
                } else {
                    throw IllegalArgumentException("Key must be string")
                }
            }

            else -> {
                if (value::class.isData) {
                    val constructor = value::class.primaryConstructor
                        ?: throw IllegalArgumentException("No primary constructor found")

                    val map = constructor.parameters.associate { param ->
                        val propValue = value::class
                            .memberProperties
                            .first { it.name == param.name }
                            .call(value)
                        param.name!! to toJsonValue(propValue)
                    }


                    return JsonObject(map)
                }
                throw IllegalArgumentException("Value must be a data class")

            }

        }
    }

}
