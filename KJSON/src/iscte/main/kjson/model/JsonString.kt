package iscte.main.kjson.model

data class JsonString(override val data: String) : JsonValue {
    override fun toJsonString(): String = "\"$data\""

    override fun equals(other: Any?): Boolean {
        require(other is JsonString)
        return this.data == other.data
    }

    override fun hashCode(): Int {
        return this.data.hashCode()
    }

    operator fun plus(other: JsonString): JsonString {
        return JsonString(this.data + other.data)
    }

    operator fun plus(other: JsonNumber): JsonString {
        return JsonString(this.data + other.data.toString())
    }

    operator fun plus(other: JsonBoolean): JsonString {
        return JsonString(this.data + other.data.toString())
    }

    operator fun plus(other: JsonNull): JsonString {
        return JsonString(this.data + other.data.toString())
    }
}