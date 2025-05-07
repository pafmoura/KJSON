package iscte.main.kjson.model

data class JsonBoolean(override val data: Boolean) : JsonValue {
    operator fun not(): JsonBoolean {
        return JsonBoolean(!this.data)
    }
}