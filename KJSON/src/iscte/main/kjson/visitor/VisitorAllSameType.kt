import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonNull
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue
import iscte.main.kjson.visitor.JsonVisitor
import kotlin.reflect.KClass

/**
 * Visitor that checks if all elements in a JSON structure are of the same type.
 *
 * This visitor traverses the JSON structure and compares the class of each element.
 * If all elements are of the same type, it returns true.
 */
class VisitorAllSameType : JsonVisitor {
    private var firstElementClass: KClass<out JsonValue>? = null
    private var isValid = true

    /**
     * Visits a JSON value and checks if it is of the same type as the first element.
     *
     * @param value The JSON value to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(value: JsonValue): Boolean {
        if (firstElementClass == null)
            firstElementClass = value::class

        isValid = isValid && value::class == firstElementClass && value !is JsonNull
        return false
    }


    /**
     * Visits a JSON object and checks if it is of the same type as the first element.
     *
     * @param obj The JSON object to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(obj: JsonObjectBase): Boolean {
        if (firstElementClass != null)
            return true

        firstElementClass = obj::class
        isValid = isValid && obj::class == firstElementClass
        return false
    }

    /**
     * Visits a JSON array and checks if it is of the same type as the first element.
     */
    override fun visit(array: JsonArrayBase): Boolean {
        if (firstElementClass != null)
            return true

        firstElementClass = array::class
        isValid = isValid && array::class == firstElementClass
        return false
    }

    fun isValid() = isValid
}