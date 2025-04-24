package iscte.main.kjson.model

abstract class JsonValue(private val _data : Any) {
    val data get() = _data
    fun toJsonString(): String = this.data.toString()
    fun accept(visitor: JsonVisitor) {
        when (this) {
            is JsonObjectBase -> {
                visitor.visit(this)
                this.entries.forEach { entry ->
                    visitor.visit(entry)
                    entry.value.accept(visitor)
                }
            }
            is JsonArrayBase -> {
                this.forEach { value ->
                    value.accept(visitor)
                }
            }
            else -> visitor.visit(this)
        }
    }
}