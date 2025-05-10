package kjson.model

/**
 * Represents a JSON null value.
 *
 * This class implements the [JsonValue] interface, which means it can be used in JSON structures.
 */
object JsonNull : JsonValue {
    override val data: Any? = null
}