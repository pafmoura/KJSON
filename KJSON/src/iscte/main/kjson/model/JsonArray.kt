package iscte.main.kjson.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

interface JsonArrayBase : JsonValue, List<JsonValue> {
    override val data: List<JsonValue> //Pode ser mais bonito?

    override fun toJsonString(): String {
        return data.joinToString(
            separator = ", ", prefix = "[", postfix = "]"
        ) { it.toJsonString() }
    }

    override fun accept(visitor: JsonVisitor) {
        data.forEach { element ->
            when (visitor) {
                is JsonValueVisitor -> visitor.visit(element)
            }
        }
    }

    fun accept(action: (JsonValue) -> Unit) {
        data.forEach { element ->
            action(element)
        }
    }

    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }


}


class JsonArray(override val data: List<JsonValue>) : JsonArrayBase, List<JsonValue> by data {
    operator fun plus(other: JsonArray): JsonArray {
        return JsonArray(this.data + other.data)
    }
}


class MutableJsonArray(val _data: MutableList<JsonValue>) : JsonArrayBase, MutableList<JsonValue> by _data {

    override val data: List<JsonValue> get() = _data

    operator fun plus(other: MutableJsonArray): MutableJsonArray {
        return MutableJsonArray((_data + other._data).toMutableList())
    }
}