package kjson.model

/**
 * Represents a JSON string value.
 *
 * @property data The string value.
 *
 *
 * This class implements the [JsonValue] interface, which means it can be used in JSON structures.
 */
data class JsonString(override val data: String) : JsonValue {

    /**
     * Converts the [JsonString] to a JSON string representation.
     * Overrides the `toJsonString` method from the [JsonValue] interface.
     *
     * @param pretty A flag indicating whether to format the JSON string with indentation.
     * Applies to JsonObject
     *
     * @return A JSON string representation of the [JsonString].
     *
     */
    override fun toJsonString(pretty: Boolean): String = "\"$data\""

    /**
     * Overrides operator for concatenate string operation with a JsonString
     *
     * @param other The other [JsonValue] to concatenate. In this case, it should be a [JsonString].
     *
     * @return A new [JsonString] representing the concatenation of this string and the other string.
     *
     * **/

    operator fun plus(other: JsonString): JsonString {
        return JsonString(this.data + other.data)
    }


    /**
     * Overrides operator for concatenate string operation with a JsonNumber
     *
     * @param other The other [JsonValue] to concatenate. In this case, it should be a [JsonNumber].
     *
     * @return A new [JsonString] representing the concatenation of this string and the other number.
     */
    operator fun plus(other: JsonNumber): JsonString {
        return JsonString(this.data + other.data.toString())
    }

    /**
     * Overrides operator for concatenate string operation with a JsonBoolean
     *
     * @param other The other [JsonValue] to concatenate. In this case, it should be a [JsonBoolean].
     *
     * @return A new [JsonString] representing the concatenation of this string and the other boolean.
     */
    operator fun plus(other: JsonBoolean): JsonString {
        return JsonString(this.data + other.data.toString())
    }

    /**
     * Overrides operator for concatenate string operation with a JsonNull
     *
     * @param other The other [JsonValue] to concatenate. In this case, it should be a [JsonNull].
     *
     * @return A new [JsonString] representing the concatenation of this string and the other null.
     */
    operator fun plus(other: JsonNull): JsonString {
        return JsonString(this.data + other.data.toString())
    }
}