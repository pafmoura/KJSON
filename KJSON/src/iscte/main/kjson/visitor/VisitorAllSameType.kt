package iscte.main.kjson.visitor

import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonNull
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue
import iscte.main.kjson.model.MutableJsonArray
import iscte.main.kjson.model.MutableJsonObject
import kotlin.reflect.KClass


class VisitorAllSameType : JsonVisitor {
    private var firstElementClass: KClass<out JsonValue>? = null
    private var isValid = true

    override fun visit(value: JsonValue): Boolean {
        if (firstElementClass == null)
            firstElementClass = value::class

        isValid = isValid && value::class == firstElementClass && value !is JsonNull
        return false
    }

    //Para correr deep ou first level...?
    /*
    override fun visit(obj: JsonObjectBase): Boolean {
        if (firstElementClass == null)
            firstElementClass = obj::class

        isValid = isValid && obj::class == firstElementClass
        return false
    }
    //Para correr deep ou first level...?
    override fun visit(array: JsonArrayBase): Boolean {
        if (firstElementClass == null)
            firstElementClass = array::class

        isValid = isValid && array::class == firstElementClass
        return false
    }
*/
    fun isValid() = isValid
}

//
//class VisitorAllSameTypeObject : JsonVisitor {
//    private var firstElementClass: KClass<out JsonValue>? = null
//    private var isValid = true
//
//    override fun visit(value: JsonValue): Boolean {
//        if (firstElementClass == null)
//            firstElementClass = value::class
//
//        isValid = isValid && value::class == firstElementClass && value !is JsonNull
//        return false
//    }
//
//    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
//        val value = entry.value
//        isValid = when (value) {
//            is JsonArrayBase -> isValid && value.isAllSameType()
//
//            is JsonObjectBase -> isValid && value.isAllSameType()
//
//            else -> isValid && value::class == firstElementClass && value !is JsonNull
//        }
//        return false
//    }
//
//    override fun visit(array: JsonArrayBase): Boolean = true
//
//    fun isValid(): Boolean = isValid
//}
//
//class VisitorAllSameTypeArray : JsonVisitor {
//    private var firstElementClass: KClass<out JsonValue>? = null
//    private var isValid = true
//
//    override fun visit(value: JsonValue): Boolean {
//        if (firstElementClass == null)
//            firstElementClass = value::class
//
//        isValid = isValid && value::class == firstElementClass && value !is JsonNull
//        return false
//    }
//
//
//    override fun visit(obj: JsonObjectBase): Boolean {
//        isValid = isValid && obj.isAllSameType()
//        return true
//    }
//
//    fun isValid(): Boolean = isValid
//}