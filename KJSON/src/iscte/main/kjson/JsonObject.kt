package iscte.main.kjson

interface JsonObjectBase : JsonValue, Map<String, JsonValue> {
    override fun toJsonString(): String {
        return entries.joinToString(
            separator = ", ", prefix = "{", postfix = "}"
        ) { "\"${it.key}\": ${it.value.toJsonString()}" }
    }


    override fun accept(visitor: (Map.Entry<String, JsonValue>) -> Unit) {
        entries.forEach { entry ->
            visitor(entry)
            entry.value.accept(visitor)
        }
    }

    fun isValid(): Boolean {
        var valid = true
        if (keys.toSet().size != keys.size) {
            return false
        }
        accept { entry ->
            if (entry.key.isEmpty()) {
                valid = false
            }
            // redundante
            // if (entry.value !is JsonValue) {
            //      valid = false
            //  }
        }
        return valid
    }


}

class MutableJsonObject(
    val properties: MutableMap<String, JsonValue> = mutableMapOf()
) : MutableMap<String, JsonValue> by properties, JsonObjectBase {
    override val data: Map<String, JsonValue> get() = properties
}

class JsonObject(
    val properties: Map<String, JsonValue> = mapOf()
) : Map<String, JsonValue> by properties, JsonObjectBase {
    override val data: Map<String, JsonValue> get() = properties


}