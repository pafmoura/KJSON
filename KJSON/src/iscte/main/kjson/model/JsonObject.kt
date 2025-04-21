package iscte.main.kjson.model

interface JsonObjectBase : JsonValue, Map<String, JsonValue> {

    override fun toJsonString(): String {
        return entries.joinToString(
            separator = ", ", prefix = "{", postfix = "}"
        ) { "\"${it.key}\": ${it.value.toJsonString()}" }
    }

    override fun accept(visitor: JsonVisitor) {
        entries.forEach { entry ->
            visitor.visit(entry)
            entry.value.accept(visitor)
        }
    }

    fun isValidObject() : Boolean {
        val visitor = VisitorValidObject()
        this.accept(visitor)
        return visitor.isValid()
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