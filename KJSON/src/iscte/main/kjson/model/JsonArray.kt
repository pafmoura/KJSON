package iscte.main.kjson.model


import VisitorAllSameType
import iscte.main.kjson.visitor.VisitorFilterArray
import iscte.main.kjson.visitor.VisitorMapArray


/**
 * Abstract base class representing a JSON array.
 * Provides common functionality for JSON array manipulation and traversal.
 *
 * @property list The underlying list of `JsonValue` elements.
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

    open fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonArrayBase {
        return map(valueAction) { key -> key }
    }

    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }

    fun get(index: Int): JsonValue {
        return list[index]
    }

    fun isNotEmpty(): Boolean {
        return list.isNotEmpty()
    }

    fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    fun numberOfProperties(): Int {
        return list.size
    }

    abstract fun subList(fromIndex: Int, toIndex: Int): JsonArrayBase

    abstract operator fun plus(other: JsonArrayBase): JsonArrayBase

    override fun equals(other: Any?): Boolean {
        require(other is JsonArrayBase)
        return this.data == other.data
    }

    override fun hashCode(): Int {
        return this.data.hashCode()
    }
}


class JsonArray(
    override val list: List<JsonValue>
) : JsonArrayBase(list) {

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonArray {
        return super.filter(valuePredicate, keyPredicate) as JsonArray
    }

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonArray {
        return super.filter(valuePredicate) as JsonArray
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonArray {
        return super.map(valueAction, keyAction) as JsonArray
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonArray {
        return super.map(valueAction) as JsonArray
    }

    override operator fun plus(other: JsonArrayBase): JsonArray {
        return JsonArray(this.data + other.data)
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): JsonArray {
        return JsonArray(this.data.subList(fromIndex, toIndex))
    }
}

class MutableJsonArray(
    override val list: MutableList<JsonValue>
) : JsonArrayBase(list) {

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): MutableJsonArray {
        return super.filter(valuePredicate, keyPredicate) as MutableJsonArray
    }

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): MutableJsonArray {
        return super.filter(valuePredicate) as MutableJsonArray
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): MutableJsonArray {
        return super.map(valueAction, keyAction) as MutableJsonArray
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue
    ): MutableJsonArray {
        return super.map(valueAction) as MutableJsonArray
    }

    override operator fun plus(other: JsonArrayBase): MutableJsonArray {
        return MutableJsonArray((list + other.data).toMutableList())
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): JsonArray {
        return JsonArray(this.data.subList(fromIndex, toIndex))
    }

    fun add(value: JsonValue): Boolean {
        return list.add(value)
    }

    fun removeAt(index: Int): JsonValue {
        return list.removeAt(index)
    }

    fun clear() {
        list.clear()
    }
}

