package iscte.main.kjson


interface JsonValue {
    val value : Any?
        get() = TODO("Needs Refractoring!")

    fun toJsonString(): String = this.value.toString()
    fun acceptValue(visitor: (JsonValue) -> Unit): Unit = visitor(this)
    fun acceptEntry(visitor: (Map.Entry<String, JsonValue>) -> Unit) {}
}

object JsonNull : JsonValue {
    override val value: Any? = null
}

data class JsonString(override val value: String) : JsonValue{
    override fun toJsonString(): String = "\"$value\""
}

data class JsonNumber(override val value: Number) : JsonValue

data class JsonBoolean(override val value: Boolean) : JsonValue