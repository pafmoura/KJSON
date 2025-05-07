package iscte.main.kjson.model

import iscte.main.kjson.visitor.JsonVisitor


interface JsonValue {

    val data: Any?

    fun toJsonString(): String = this.data.toString()

    fun accept(visitor: JsonVisitor) {
        when (this) {
            is JsonObjectBase -> {
                if (visitor.visit(this)) {
                    return
                }

                (this as JsonObjectBase).data.entries.forEach { entry ->
                    visitor.visit(entry)
                    entry.value.accept(visitor)
                }
            }

            is JsonArrayBase -> {
                if (visitor.visit(this)) {
                    return
                }
                this.data.forEach { value ->
                    value.accept(visitor)
                }
            }

            else -> {
                visitor.visit(this)
            }
        }
    }
}