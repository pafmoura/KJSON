package iscte.main.kjson.model

data class JsonBoolean(override val data: Boolean) : JsonValue {
    override fun equals(other: Any?): Boolean {
        require(other is JsonBoolean)
        return this.data == other.data
    }

    override fun hashCode(): Int {
        return this.data.hashCode()
    }

    operator fun not(): JsonBoolean {
        return JsonBoolean(!this.data)
    }
}