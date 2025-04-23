package iscte.main.kjson.model

import kotlin.reflect.KClass

interface JsonVisitor

interface JsonValueVisitor : JsonVisitor {
    fun visit(element: JsonValue) {}
}

interface JsonEntryVisitor : JsonVisitor {
    fun visit(element: Map.Entry<String, JsonValue>) {}
}

class VisitorValidObject : JsonValueVisitor, JsonEntryVisitor {
    private val keys = mutableSetOf<String>()
    private var isValid = true

    override fun visit(element: Map.Entry<String, JsonValue>): Unit {
        isValid = isValid && keys.add(element.key)
    }

    fun isValid() = isValid
}

class VisitorAllSameType : JsonValueVisitor, JsonEntryVisitor {
    private var firstElementClass: KClass<out JsonValue>? = null
    private var isValid = true

    override fun visit(element: JsonValue) {
        if (firstElementClass == null)
            firstElementClass = element::class

        isValid = isValid && element::class == firstElementClass && element !is JsonNull
    }

    override fun visit(element: Map.Entry<String, JsonValue>): Unit {
        visit(element.value)
    }

    fun isValid() = isValid

}

