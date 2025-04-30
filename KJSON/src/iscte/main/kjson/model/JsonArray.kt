package iscte.main.kjson.model

import iscte.main.kjson.visitor.VisitorAllSameType
import iscte.main.kjson.visitor.VisitorFilterArray
import iscte.main.kjson.visitor.VisitorMapArray
import iscte.main.kjson.visitor.VisitorValidArray

abstract class JsonArrayBase(
    val list: List<JsonValue> = listOf<JsonValue>()
) : JsonValue, List<JsonValue> {

    override val data: List<JsonValue> get() = list

    override fun toJsonString(): String {
        return data.joinToString(
            separator = ", ", prefix = "[", postfix = "]"
        ) { it.toJsonString() }
    }


    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }

    fun isValid(): Boolean {
        val visitor = VisitorValidArray()
        this.accept(visitor)
        return visitor.isValid()
    }

    open fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonArrayBase {
        val visitor = VisitorFilterArray(valuePredicate, keyPredicate)
        accept(visitor)
        return visitor.getResult()
    }

    open fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonArrayBase {
        val visitor = VisitorMapArray(valueAction, keyAction)
        accept(visitor)
        return visitor.getResult()
    }
}


class JsonArray(
    list: List<JsonValue>
) : JsonArrayBase(list), List<JsonValue> by list {

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonArray {
        return super.filter(valuePredicate, keyPredicate) as JsonArray
    }

    fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonArray {
        return super.filter(valuePredicate) { key -> true } as JsonArray
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonArray {
        return super.map(valueAction, keyAction) as JsonArray
    }

    fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonArray {
        return super.map(valueAction) { key -> key } as JsonArray
    }

    operator fun plus(other: JsonArray): JsonArray {
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
    list: MutableList<JsonValue>
) : JsonArrayBase(list), MutableList<JsonValue> by list {

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): MutableJsonArray {
        return super.filter(valuePredicate, keyPredicate) as MutableJsonArray
    }

    fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): MutableJsonArray {
        return super.filter(valuePredicate) { key -> true } as MutableJsonArray
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): MutableJsonArray {
        return super.map(valueAction, keyAction) as MutableJsonArray
    }

    fun map(
        valueAction: (JsonValue) -> JsonValue
    ): MutableJsonArray {
        return super.map(valueAction) { key -> key } as MutableJsonArray
    }

    operator fun plus(other: MutableJsonArray): MutableJsonArray {
        return MutableJsonArray((list + other.list).toMutableList())
    }


}

