package iscte.main.kjson

interface JsonObjectBase : JsonValue, Map<String, JsonValue> {
    override fun toJsonString(): String {
        return entries.joinToString(
            separator = ", ", prefix = "{", postfix = "}"
        ) { "\"${it.key}\": ${it.value.toJsonString()}" }
    }

    override fun acceptValue(visitor: (JsonValue) -> Unit) {
        entries.forEach { entry ->
            entry.value.acceptValue(visitor)
        }
    }

    override fun acceptEntry(visitor: (Map.Entry<String, JsonValue>) -> Unit) {
        entries.forEach { entry ->
            visitor(entry)
            entry.value.acceptEntry(visitor)
        }
    }

}

class MutableJsonObject(
    val properties: MutableMap<String, JsonValue> = mutableMapOf()
) : MutableMap<String, JsonValue> by properties, JsonObjectBase {
    override val value: Map<String, JsonValue> get() = properties
}

class JsonObject(
    val properties: Map<String, JsonValue> = mapOf()
) : Map<String, JsonValue> by properties, JsonObjectBase {
    override val value: Map<String, JsonValue> get() = properties

}