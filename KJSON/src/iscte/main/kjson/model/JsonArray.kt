package iscte.main.kjson.model

class JsonArray(override val data: List<JsonValue>) : JsonValue, List<JsonValue> by data {

    override fun toJsonString(): String {
        return data.joinToString(
            separator = ", ", prefix = "[", postfix = "]"
        ) { it.toJsonString() }
    }

    override fun accept(visitor : JsonVisitor) {
        data.forEach {
            visitor.visit(it)
        }
    }

    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }


}