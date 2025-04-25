package iscte.main.kjson.visitor

import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue
import kotlin.reflect.KClass

//class VisitorValidObject : JsonVisitor {
//    private val keys = mutableSetOf<String>()
//    private var isValid = true
//
//    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
//        isValid = isValid && keys.add(entry.key)
//        return false
//    }
//
//    fun isValid() = isValid
//}


class VisitorValidObject : JsonVisitor {
    private val keys = mutableSetOf<String>()
    private var isValid = true

    override fun visit(value: JsonValue): Boolean {
        return false
    }

    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
        val value = entry.value
        isValid = when (value) {
            is JsonArrayBase -> isValid && value.isValid()

            is JsonObjectBase -> isValid && value.isValid()

            else -> isValid && keys.add(entry.key)
        }
        return false
    }

    override fun visit(array: JsonArrayBase): Boolean = true

    fun isValid(): Boolean = isValid
}

class VisitorValidArray : JsonVisitor {
    private var isValid = true

    override fun visit(obj: JsonObjectBase): Boolean {
        isValid = isValid && obj.isValid()
        return true
    }

    fun isValid(): Boolean = isValid
}