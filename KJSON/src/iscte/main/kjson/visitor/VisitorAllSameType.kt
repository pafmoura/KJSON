import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonNull
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue
import iscte.main.kjson.visitor.JsonVisitor
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

    override fun visit(obj: JsonObjectBase): Boolean {
        if (firstElementClass != null)
            return true

        firstElementClass = obj::class
        isValid = isValid && obj::class == firstElementClass
        return false
    }

    //Para correr deep ou first level...?
    override fun visit(array: JsonArrayBase): Boolean {
        if (firstElementClass != null)
            return true

        firstElementClass = array::class
        isValid = isValid && array::class == firstElementClass
        return false
    }

    fun isValid() = isValid
}