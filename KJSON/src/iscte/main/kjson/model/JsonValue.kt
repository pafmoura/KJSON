package iscte.main.kjson.model


interface JsonValue {

    val data: Any?

    fun toJsonString(): String = this.data.toString()

    fun accept(visitor: JsonVisitor) {
        when (this) {
            is JsonObjectBase -> {
                if(visitor.visit(this)){
                    return
                }

                this.entries.forEach { entry ->
                    visitor.visit(entry)
                    entry.value.accept(visitor)
                }
            }

            is JsonArrayBase -> {
                if(visitor.visit(this)){
                    return
                }
                this.forEach { value ->
                    value.accept(visitor)
                }
            }

            else -> {
                visitor.visit(this)
            }
        }
    }
}