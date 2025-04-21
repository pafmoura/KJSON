package iscte.main.kjson.model

interface JsonValue {
    val data: Any?
    fun toJsonString(): String = this.data.toString()
    fun accept(visitor: JsonVisitor) {}
}