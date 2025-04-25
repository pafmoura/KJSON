package iscte.main.kjson.model

abstract class JsonObjectBase(
    val properties: Map<String, JsonValue> = mapOf()
) : JsonValue, Map<String, JsonValue> {

    override val data: Map<String, JsonValue> get() = properties

    override fun toJsonString(): String {
        return entries.joinToString(
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

    open fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonObjectBase {
        val visitor = VisitorMapObject(valueAction, keyAction)
        accept(visitor)
        return visitor.getResult()
    }

    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }

    fun isValidObject(): Boolean {
        val visitor = VisitorValidObject()
        this.accept(visitor)
        return visitor.isValid()
    }
}

class MutableJsonObject(
    properties: MutableMap<String, JsonValue> = mutableMapOf()
) : MutableMap<String, JsonValue> by properties, JsonObjectBase(properties) {

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): MutableJsonObject {
        return super.filter(valuePredicate, keyPredicate) as MutableJsonObject
    }

    fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): MutableJsonObject {
        return super.filter(valuePredicate) { key -> true } as MutableJsonObject
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): MutableJsonObject {
        return super.map(valueAction, keyAction) as MutableJsonObject
    }

    fun map(
        valueAction: (JsonValue) -> JsonValue
    ): MutableJsonObject {
        return super.map(valueAction){key -> key} as MutableJsonObject
    }
}

class JsonObject(
    properties: Map<String, JsonValue> = mapOf()
) : Map<String, JsonValue> by properties, JsonObjectBase(properties) {

    override fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonObject {
        return super.filter(valuePredicate, keyPredicate) as JsonObject
    }

    fun filter(
        valuePredicate: (JsonValue) -> Boolean
    ): JsonObject {
        return super.filter(valuePredicate) { key -> true } as JsonObject
    }

    override fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonObject {
        return super.map(valueAction, keyAction) as JsonObject
    }

    fun map(
        valueAction: (JsonValue) -> JsonValue
    ): JsonObject {
        return super.map(valueAction){key -> key} as JsonObject
    }
}