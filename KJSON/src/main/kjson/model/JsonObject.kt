package kjson.model

import kjson.visitor.VisitorAllSameType
import kjson.visitor.VisitorFilterObject
import kjson.visitor.VisitorMapObject


/**
 * Represents a base class for JSON objects.
 *
 * @property properties A map of string keys to JSON values.
 *
 * This class implements the [JsonValue] interface, which means it can be used in JSON structures.
 * It provides methods for filtering and mapping the properties of the JSON object.
 *
 */
abstract class JsonObjectBase(
    protected open val properties: Map<String, JsonValue> = mapOf()
) : JsonValue {

    /**
     * Returns the properties of the JSON object as a map.
     *
     * @return A map of string keys to JSON values.
     */
    override val data: Map<String, JsonValue> get() = properties

    /**
     * Returns a string representation of the JSON object in JSON format.
     *
     * @param pretty A boolean indicating whether to format the JSON string with indentation and line breaks.
     * Applies to JsonObject.
     *
     * @return A string representing the JSON object in JSON format.
     */


     override fun toJsonString(pretty: Boolean): String{
         if (pretty) {
             return properties.entries.joinToString(
                 separator = ",\n", prefix = "\n{\n", postfix = "\n}\n"
             ) { "\"${it.key}\": ${it.value.toJsonString(true)}" }.prependIndent("\t")
         }
         return properties.entries.joinToString(
             separator = ", ", prefix = "{", postfix = "}"
         ) { "\"${it.key}\": ${it.value.toJsonString()}" }
    }

    /**
     * Filters the properties of the JSON object based on the provided predicates.
     *
     * @param valuePredicate A predicate function that takes a JSON value and returns a boolean indicating whether to include it.
     * @param keyPredicate A predicate function that takes a string key and returns a boolean indicating whether to include it.
     * There is another overload of this function that only filters by value.
     *
     * @return A new JSON object containing only the properties that match the predicates.

     */
    open fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonObjectBase {
        val visitor = VisitorFilterObject(valuePredicate, keyPredicate)
        accept(visitor)
        return visitor.getResult()
    }

    /**
     * Filters the properties of the JSON object based on the provided value predicate.
     *
     * @param valuePredicate A predicate function that takes a JSON value and returns a boolean indicating whether to include it.
     * The key is not filtered in this case.
     * @return A new JSON object containing only the properties that match the value predicate.
     */

    open fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonObjectBase {
        return filter(valuePredicate) { key -> true }

    }

    /**
     * Maps the properties of the JSON object using the provided value and key actions.
     *
     * @param valueAction A function that takes a JSON value and returns a transformed JSON value.
     * @param keyAction A function that takes a string key and returns a transformed string key.
     * There is another overload of this function that only maps by value.
     *
     * @return A new JSON object with the mapped properties.
     */
    open fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonObjectBase {
        val visitor = VisitorMapObject(valueAction, keyAction)
        accept(visitor)
        return visitor.getResult()
    }

    /**
     * Maps the properties of the JSON object using the provided value action.
     *
     * @param valueAction A function that takes a JSON value and returns a transformed JSON value.
     * The key is not transformed in this case.
     *
     * @return A new JSON object with the mapped properties.
     */
    open fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonObjectBase {
        return map(valueAction) { key -> key }
    }

    /**
     * Checks if all values in the JSON object properties are of the same type.
     *
     * @return True if all values are of the same type, false otherwise.
     *
     * This function uses a visitor pattern to traverse the JSON object and check the types of the values.
     */
    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }

    /**
     * Returns the value associated with the specified key in the JSON object.
     *
     * @param key The key whose associated value is to be returned.
     *
     * @return The JSON value associated with the specified key, or null if the key does not exist.
     *
     */
    fun get(key: String): JsonValue? {
        return properties[key]
    }

    /**
     * Checks if the JSON object has any properties.
     *
     * @return True if the JSON object has properties, false otherwise.

     */
    fun isNotEmpty(): Boolean {
        return properties.isNotEmpty()
    }

    /**
     * Checks if the JSON object has no properties.
     *
     * @return True if the JSON object has no properties, false otherwise.
     */
    fun isEmpty(): Boolean {
        return properties.isEmpty()
    }

    /**
     * Returns the number of properties in the JSON object.
     *
     * @return The number of properties in the JSON object.
     */
    fun numberOfProperties(): Int {
        return properties.size
    }

    /**
     * Checks if two JSON objects are equal.
     *
     * @param other The other object to compare with.
     *
     * @return True if the two JSON objects are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        require(other is JsonObjectBase)
        return this.properties == other.properties
    }

    /**
     * Returns the hash code of the JSON object.
     *
     * @return The hash code of the JSON object.
     */
    override fun hashCode(): Int {
        return properties.hashCode()
    }
}


/**
 * Represents a mutable JSON object.
 *
 * @property properties A mutable map of string keys to JSON values.
 *
 * This class extends the [JsonObjectBase] class and provides methods for modifying the properties of the JSON object.
 * It allows adding, removing, and clearing properties.
 *
 */
class MutableJsonObject(
    override val properties: MutableMap<String, JsonValue> = mutableMapOf()
) : JsonObjectBase(properties) {

    /**
     * Filters the properties of the JSON object based on the provided predicates.
     *
     * @param valuePredicate A predicate function that takes a JSON value and returns a boolean indicating whether to include it.
     * @param keyPredicate A predicate function that takes a string key and returns a boolean indicating whether to include it.
     *
     * @return A new mutable JSON object containing only the properties that match the predicates.
     */
    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): MutableJsonObject {
        return super.filter(valuePredicate, keyPredicate) as MutableJsonObject
    }

    /**
     * Filters the properties of the JSON object based on the provided value predicate.
     *
     * @param valuePredicate A predicate function that takes a JSON value and returns a boolean indicating whether to include it.
     *
     * @return A new mutable JSON object containing only the properties that match the value predicate.
     */
    override fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): MutableJsonObject {
        return super.filter(valuePredicate) as MutableJsonObject
    }

    /**
     * Maps the properties of the JSON object using the provided value and key actions.
     *
     * @param valueAction A function that takes a JSON value and returns a transformed JSON value.
     * @param keyAction A function that takes a string key and returns a transformed string key.
     *
     * @return A new mutable JSON object with the mapped properties.
     */
    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): MutableJsonObject {
        return super.map(valueAction, keyAction) as MutableJsonObject
    }

    /**
     * Maps the properties of the JSON object using the provided value action.
     *
     * @param valueAction A function that takes a JSON value and returns a transformed JSON value.
     *
     * @return A new mutable JSON object with the mapped properties.
     */
    override fun map(
        valueAction: (JsonValue) -> JsonValue
    ): MutableJsonObject {
        return super.map(valueAction) as MutableJsonObject
    }

    /**
     * Adds a new property to the JSON object or updates an existing property.
     *
     * @param key The key of the property to add or update.
     * @param value The JSON value to associate with the key.
     *
     * @return The previous value associated with the key, or null if there was no mapping for the key.
     */
    fun put(key: String, value: JsonValue): JsonValue? {
        return properties.put(key, value)
    }

    /**
     * Removes a property from the JSON object.
     *
     * @param key The key of the property to remove.
     *
     * @return The value that was associated with the key, or null if there was no mapping for the key.
     */
    fun remove(key: String): JsonValue? {
        return properties.remove(key)
    }

    /**
     * Clears all properties from the JSON object.
     *
     * This method removes all key-value pairs from the JSON object, leaving it empty.
     */
    fun clear() {
        properties.clear()
    }
}

/**
 * Represents an immutable JSON object.
 *
 * @property properties A map of string keys to JSON values.
 *
 * This class extends the [JsonObjectBase] class and provides methods for accessing the properties of the JSON object.
 * It does not allow modifying the properties after creation.
 */
class JsonObject(
    override val properties: Map<String, JsonValue> = mapOf()
) : JsonObjectBase(properties) {

    /**
     * Filters the properties of the JSON object based on the provided predicates.
     *
     * @param valuePredicate A predicate function that takes a JSON value and returns a boolean indicating whether to include it.
     * @param keyPredicate A predicate function that takes a string key and returns a boolean indicating whether to include it.
     *
     * @return A new immutable JSON object containing only the properties that match the predicates.
     */
    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonObject {
        return super.filter(valuePredicate, keyPredicate) as JsonObject
    }

    /**
     * Filters the properties of the JSON object based on the provided value predicate.
     *
     * @param valuePredicate A predicate function that takes a JSON value and returns a boolean indicating whether to include it.
     *
     * @return A new immutable JSON object containing only the properties that match the value predicate.
     */
    override fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonObject {
        return super.filter(valuePredicate) as JsonObject
    }

    /**
     * Maps the properties of the JSON object using the provided value and key actions.
     *
     * @param valueAction A function that takes a JSON value and returns a transformed JSON value.
     * @param keyAction A function that takes a string key and returns a transformed string key.
     *
     * @return A new immutable JSON object with the mapped properties.
     */
    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonObject {
        return super.map(valueAction, keyAction) as JsonObject
    }

    /**
     * Maps the properties of the JSON object using the provided value action.
     *
     * @param valueAction A function that takes a JSON value and returns a transformed JSON value.
     *
     * @return A new immutable JSON object with the mapped properties.
     */
    override fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonObject {
        return super.map(valueAction) as JsonObject
    }
}