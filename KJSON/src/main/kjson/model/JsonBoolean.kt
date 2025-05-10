package kjson.model

/**
 * Represents a JSON boolean value.
 *
 * @property data The boolean value.
 *
 * This class implements the [JsonValue] interface, which means it can be used in JSON structures.
 */
data class JsonBoolean(override val data: Boolean) : JsonValue {
    operator fun not(): JsonBoolean {
        return JsonBoolean(!this.data)
    }
}