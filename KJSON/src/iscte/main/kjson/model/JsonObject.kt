package iscte.main.kjson.model

import iscte.main.kjson.visitor.VisitorAllSameType
import iscte.main.kjson.visitor.VisitorFilterObject
import iscte.main.kjson.visitor.VisitorMapObject
import iscte.main.kjson.visitor.VisitorValidObject

abstract class JsonObjectBase(
    protected open val properties: Map<String, JsonValue> = mapOf()
) : JsonValue {

    override val data: Map<String, JsonValue> get() = properties

    override fun toJsonString(): String {
        return properties.entries.joinToString(
            separator = ", ", prefix = "{", postfix = "}"
        ) { "\"${it.key}\": ${it.value.toJsonString()}" }
    }

    open fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonObjectBase {
        val visitor = VisitorFilterObject(valuePredicate, keyPredicate)
        accept(visitor)
        return visitor.getResult()
    }

    open fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonObjectBase {
        return filter(valuePredicate) { key -> true }

    }

    open fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonObjectBase {
        val visitor = VisitorMapObject(valueAction, keyAction)
        accept(visitor)
        return visitor.getResult()
    }

    open fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonObjectBase {
        return map(valueAction) { key -> key }
    }

    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }

    fun isValid(): Boolean {
        val visitor = VisitorValidObject()
        this.accept(visitor)
        return visitor.isValid()
    }

    fun get(key: String): JsonValue? {
        return properties[key]
    }

    fun isNotEmpty(): Boolean {
        return properties.isNotEmpty()
    }

}

class MutableJsonObject(
    override val properties: MutableMap<String, JsonValue> = mutableMapOf()
) : JsonObjectBase(properties) {

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): MutableJsonObject {
        return super.filter(valuePredicate, keyPredicate) as MutableJsonObject
    }

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): MutableJsonObject {
        return super.filter(valuePredicate) as MutableJsonObject
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): MutableJsonObject {
        return super.map(valueAction, keyAction) as MutableJsonObject
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue
    ): MutableJsonObject {
        return super.map(valueAction) as MutableJsonObject
    }


    fun put(key: String, value: JsonValue): JsonValue? {
        return properties.put(key, value)
    }

    fun remove(key: String): JsonValue? {
        return properties.remove(key)
    }

    fun clear() {
        properties.clear()
    }

    fun isEmpty(): Boolean {
        return properties.isEmpty()
    }

    fun numberOfProperties(): Int {
        return properties.size
    }


}

class JsonObject(
    override val properties: Map<String, JsonValue> = mapOf()
) : JsonObjectBase(properties) {

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonObject {
        return super.filter(valuePredicate, keyPredicate) as JsonObject
    }

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonObject {
        return super.filter(valuePredicate) as JsonObject
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonObject {
        return super.map(valueAction, keyAction) as JsonObject
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonObject {
        return super.map(valueAction) as JsonObject
    }


}