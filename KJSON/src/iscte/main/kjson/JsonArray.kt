package iscte.main.kjson

class JsonArray(override val data: List<JsonValue>) : JsonValue, List<JsonValue> by data {

    override fun toJsonString(): String {
        return data.joinToString(
            separator = ", ", prefix = "[", postfix = "]"
        ) { it.toJsonString() }
    }

    fun allSameType(): Boolean {
        if (data.isEmpty()) return true
        val firstType = data[0]::class
        return data.all { it::class == firstType }
    }


    //TODO: Esperar pelo Prof.
    //fun accept(visitor: (JsonValue) -> Unit) {
    //    data.forEach { value ->
    //         visitor(value)
    //         //value.accept(visitor)
    //    }
//    }
}