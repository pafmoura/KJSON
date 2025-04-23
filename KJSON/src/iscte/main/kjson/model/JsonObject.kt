package iscte.main.kjson.model

interface JsonObjectBase : JsonValue, Map<String, JsonValue> {

    override fun toJsonString(): String {
        return entries.joinToString(
            separator = ", ", prefix = "{", postfix = "}"
        ) { "\"${it.key}\": ${it.value.toJsonString()}" }
    }

    override fun accept(visitor: JsonVisitor) {
        entries.forEach { entry ->

            when (visitor) {
                is JsonEntryVisitor -> {
                    visitor.visit(entry)
                    entry.value.accept(visitor)
                }

                is JsonValueVisitor -> {
                    visitor.visit(entry.value)
                    entry.value.accept(visitor)
                }
            }
        }
    }

    fun accept(action: (Map.Entry<String, JsonValue>) -> Unit) {
        entries.forEach { entry ->
            action(entry)
            if (entry.value is JsonObjectBase)
                (entry.value as JsonObjectBase).accept(action)
        }
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
    val properties: MutableMap<String, JsonValue> = mutableMapOf()
) : MutableMap<String, JsonValue> by properties, JsonObjectBase {
    override val data: Map<String, JsonValue> get() = properties




    fun filter(valuePredicate: (JsonValue) -> Boolean, keyPredicate: (String) -> Boolean = { key -> true } ): MutableJsonObject {
        val filteredMap = mutableMapOf<String, JsonValue>()
        this.accept { entry ->
            when (entry.value) {

                is JsonArray -> {
                    if (keyPredicate(entry.key)) {
                        val filteredArray = (entry.value as JsonArray).filter(valuePredicate)
                        if (filteredArray.isNotEmpty()) {
                            filteredMap.put(entry.key, filteredArray)
                        }
                    }

                    }


                else -> {
                    if (valuePredicate(entry.value) && keyPredicate(entry.key)) {
                        filteredMap.put(entry.key, entry.value)
                    }
                }

            }
        }
return MutableJsonObject(filteredMap)
    }

}

class JsonObject(
    val properties: Map<String, JsonValue> = mapOf()
) : Map<String, JsonValue> by properties, JsonObjectBase {
    override val data: Map<String, JsonValue> get() = properties


}