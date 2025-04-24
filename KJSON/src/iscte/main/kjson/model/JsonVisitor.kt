package iscte.main.kjson.model

import kotlin.reflect.KClass

interface JsonVisitor{
    fun visit(value: JsonValue)
    fun visit(entry: Map.Entry<String, JsonValue>)
}




class VisitorValidObject : JsonVisitor {
    private val keys = mutableSetOf<String>()
    private var isValid = true

    override fun visit(value: JsonValue) {

    }

    override fun visit(entry: Map.Entry<String, JsonValue>): Unit {
        isValid = isValid && keys.add(entry.key)
    }

    fun isValid() = isValid
}

class VisitorAllSameType : JsonVisitor {
    private var firstElementClass: KClass<out JsonValue>? = null
    private var isValid = true

    override fun visit(value: JsonValue) {
        if (firstElementClass == null)
            firstElementClass = value::class

        isValid = isValid && value::class == firstElementClass && value !is JsonNull
    }

    override fun visit(entry: Map.Entry<String, JsonValue>): Unit {
    }

    fun isValid() = isValid

}




