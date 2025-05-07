package iscte.main.kjson.model


import VisitorAllSameType
import iscte.main.kjson.visitor.VisitorFilterArray
import iscte.main.kjson.visitor.VisitorMapArray


/**
 * Abstract base class representing a JSON array.
 * Provides common functionality for JSON array manipulation and traversal.
 *
 * @property list The underlying list of `JsonValue` elements.
 *
 * This class implements the [JsonValue] interface, which means it can be used in JSON structures.
 */
abstract class JsonArrayBase(
    protected open val list: List<JsonValue> = listOf<JsonValue>()
) : JsonValue {

    /**
     * Returns the data of the JSON array as a list of `JsonValue`.
     */
    override val data: List<JsonValue> get() = list

    /**
     * Converts the JSON array to its JSON string representation.
     *
     * @return A JSON string representing the array.
     */
    override fun toJsonString(): String {
        return data.joinToString(
            separator = ", ", prefix = "[", postfix = "]"
        ) { it.toJsonString() }
    }

    /**
     * Filters the JSON array based on value and key predicates.
     *
     * @param valuePredicate A predicate to filter values.
     * @param keyPredicate A predicate to filter keys.
     * @return A new filtered `JsonArrayBase`.
     */
    open fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonArrayBase {
        val visitor = VisitorFilterArray(valuePredicate, keyPredicate)
        accept(visitor)
        return visitor.getResult()
    }

    /**
     * Filters the JSON array based on a value predicate.
     *
     * @param valuePredicate A predicate to filter values.
     * @return A new filtered `JsonArrayBase`.
     */
    open fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonArrayBase {
        return filter(valuePredicate) { key -> true }
    }

    /**
     * Maps the JSON array by applying actions to values and keys.
     *
     * @param valueAction A function to transform values.
     * @param keyAction A function to transform keys.
     * @return A new mapped `JsonArrayBase`.
     */
    open fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonArrayBase {
        val visitor = VisitorMapArray(valueAction, keyAction)
        accept(visitor)
        return visitor.getResult()
    }

    /**
     * Maps the JSON array by applying an action to values.
     *
     * @param valueAction A function to transform values.
     * @return A new mapped `JsonArrayBase`.
     */
    open fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonArrayBase {
        return map(valueAction) { key -> key }
    }

    /**
     * Checks if all elements in the JSON array are of the same type.
     *
     * @return `true` if all elements are of the same type, `false` otherwise.
     */
    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index The index to search.
     */
    fun get(index: Int): JsonValue {
        return list[index]
    }

    /**
     * Checks if the JSON array is not empty.
     *
     * @return `true` if the array is not empty, `false` otherwise.
     */
    fun isNotEmpty(): Boolean {
        return list.isNotEmpty()
    }

    /**
     * Checks if the JSON array is empty.
     *
     * @return `true` if the array is empty, `false` otherwise.
     */
    fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    /**
     * Returns the number of properties in the JSON array.
     *
     * @return The number of properties.
     */
    fun numberOfProperties(): Int {
        return list.size
    }

    /**
     * Returns a sublist of the JSON array from the specified range.
     *
     * @param fromIndex The starting index (inclusive).
     * @param toIndex The ending index (exclusive).
     * @return A new `JsonArrayBase` containing the specified range of elements.
     *
     **/

    abstract fun subList(fromIndex: Int, toIndex: Int): JsonArrayBase

    /**
     * Concatenate another JSON array to this JSON array.
     *
     * @param other The other JSON array to add.
     * @return A new `JsonArrayBase` containing the elements.
     */
    abstract operator fun plus(other: JsonArrayBase): JsonArrayBase


    /**
     * Checks if this JSON array is equal to another object.
     *
     * @param other The object to compare.
     * @return `true` if the objects are equal, `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        require(other is JsonArrayBase)
        return this.data == other.data
    }

    /**
     * Returns the hash code of this JSON array.
     *
     * @return The hash code of the JSON array.
     */
    override fun hashCode(): Int {
        return this.data.hashCode()
    }
}


/**
 * Concrete implementation of a JSON array.
 * Inherits from `JsonArrayBase` and provides specific functionality for JSON arrays.
 *
 * @property list The underlying list of `JsonValue` elements.
 */
class JsonArray(
    override val list: List<JsonValue>
) : JsonArrayBase(list) {

    /**
     * Filters the JSON array based on value and key predicates.
     *
     * @param valuePredicate A predicate to filter values.
     * @param keyPredicate A predicate to filter keys.
     * @return A new filtered `JsonArray`.
     *
     */
    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonArray {
        return super.filter(valuePredicate, keyPredicate) as JsonArray
    }

    /**
     * Filters the JSON array based on a value predicate.
     *
     * @param valuePredicate A predicate to filter values.
     * @return A new filtered `JsonArray`.
     */
    override fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonArray {
        return super.filter(valuePredicate) as JsonArray
    }

    /**
     * Maps the JSON array by applying actions to values and keys.
     *
     * @param valueAction A function to transform values.
     * @param keyAction A function to transform keys.
     * @return A new mapped `JsonArray`.
     */
    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonArray {
        return super.map(valueAction, keyAction) as JsonArray
    }

    /**
     * Maps the JSON array by applying an action to values.
     *
     * @param valueAction A function to transform values.
     * @return A new mapped `JsonArray`.
     */
    override fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonArray {
        return super.map(valueAction) as JsonArray
    }

    /**
     * Concatenate another JSON array to this JSON array.
     *
     * @param other The other JSON array to add.
     * @return A new `JsonArray` containing the elements.
     */
    override operator fun plus(other: JsonArrayBase): JsonArray {
        return JsonArray(this.data + other.data)
    }

    /**
     * Returns a sublist of the JSON array from the specified range.
     *
     * @param fromIndex The starting index (inclusive).
     * @param toIndex The ending index (exclusive).
     * @return A new `JsonArray` containing the specified range of elements.
     */
    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): JsonArray {
        return JsonArray(this.data.subList(fromIndex, toIndex))
    }
}

/**
 * Mutable implementation of a JSON array.
 * Inherits from `JsonArrayBase` and provides specific functionality for MUTABLE JSON arrays.
 *
 * @property list The underlying mutable list of `JsonValue` elements.
 */
class MutableJsonArray(
    override val list: MutableList<JsonValue>
) : JsonArrayBase(list) {

    /**
     * Filters the JSON array based on value and key predicates.
     *
     * @param valuePredicate A predicate to filter values.
     * @param keyPredicate A predicate to filter keys.
     * @return A new filtered `MutableJsonArray`.
     */
    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): MutableJsonArray {
        return super.filter(valuePredicate, keyPredicate) as MutableJsonArray
    }

    /**
     * Filters the JSON array based on a value predicate.
     *
     * @param valuePredicate A predicate to filter values.
     * @return A new filtered `MutableJsonArray`.
     */
    override fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): MutableJsonArray {
        return super.filter(valuePredicate) as MutableJsonArray
    }

    /**
     * Maps the JSON array by applying actions to values and keys.
     *
     * @param valueAction A function to transform values.
     * @param keyAction A function to transform keys.
     * @return A new mapped `MutableJsonArray`.
     */
    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): MutableJsonArray {
        return super.map(valueAction, keyAction) as MutableJsonArray
    }

    /**
     * Maps the JSON array by applying an action to values.
     *
     * @param valueAction A function to transform values.
     * @return A new mapped `MutableJsonArray`.
     */
    override fun map(
        valueAction: (JsonValue) -> JsonValue
    ): MutableJsonArray {
        return super.map(valueAction) as MutableJsonArray
    }

    /**
     * Concatenate another JSON array to this JSON array.
     *
     * @param other The other JSON array to add.
     * @return A new `MutableJsonArray` containing the elements.
     */
    override operator fun plus(other: JsonArrayBase): MutableJsonArray {
        return MutableJsonArray((list + other.data).toMutableList())
    }

    /**
     * Returns a sublist of the JSON array from the specified range.
     *
     * @param fromIndex The starting index (inclusive).
     * @param toIndex The ending index (exclusive).
     * @return A new `MutableJsonArray` containing the specified range of elements.
     */
    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): JsonArray {
        return JsonArray(this.data.subList(fromIndex, toIndex))
    }

    /**
     * Adds a new value to the JSON array.
     *
     * @param value The value to add.
     * @return `true` if the value was added successfully, `false` otherwise.
     */
    fun add(value: JsonValue): Boolean {
        return list.add(value)
    }

    /**
     * Remove a value from the JSON array at the specified index.
     *
     * @param index The index of the value to remove.
     *
     * @return The removed `JsonValue`.
     */
    fun removeAt(index: Int): JsonValue {
        return list.removeAt(index)
    }

    /**
     * Clears all elements from the JSON array.
     */
    fun clear() {
        list.clear()
    }
}

