package iscte.main.kjson.model

import kotlin.reflect.KClass

interface JsonVisitor {
    fun visit(element: JsonValue) {}
    fun visit(element: Map.Entry<String, JsonValue>) {}
}

class VisitorValidObject : JsonVisitor {
    private val keys = mutableSetOf<String>()
    private var isValid = true

    override fun visit(element: JsonValue) {
        element.accept(this)
    }

    override fun visit(element: Map.Entry<String, JsonValue>): Unit {
        isValid = isValid && keys.add(element.key)
    }

    fun isValid() = isValid
}

class VisitorAllSameType : JsonVisitor {
    private var firstElementClass: KClass<out JsonValue>? = null
    private var isValid = true

    override fun visit(element: JsonValue) {
        if (firstElementClass == null)
            firstElementClass = element::class

        isValid = isValid && element::class == firstElementClass && element !is JsonNull
    }

    override fun visit(element: Map.Entry<String, JsonValue>): Unit {
        element.value.accept(this)
    }

    fun isValid() = isValid

}

