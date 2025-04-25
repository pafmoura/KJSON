package iscte.main.kjson.visitor

import iscte.main.kjson.model.JsonValue

class VisitorValidObject : JsonVisitor {
    private val keys = mutableSetOf<String>()
    private var isValid = true

    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
        isValid = isValid && keys.add(entry.key)
        return false
    }

    fun isValid() = isValid
}