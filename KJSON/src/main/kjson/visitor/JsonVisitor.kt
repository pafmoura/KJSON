package kjson.visitor

import kjson.model.JsonArrayBase
import kjson.model.JsonObjectBase
import kjson.model.JsonValue

/**
 * Interface for visiting JSON values.
 *
 * This interface defines methods for visiting different types of JSON values, including objects and arrays.
 * Implementations of this interface can be used to traverse and process JSON structures in a flexible way.
 *
 * It uses a boolean return type to indicate if the visitor should continue visiting the JSON structure.
 * If the visitor returns true, the traversal will stop.
 */
interface JsonVisitor {
    /**
     * Visits a JSON value.
     *
     * @param value The JSON value to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     * Note: At this moment, the return value is not used.
     */
    fun visit(value: JsonValue): Boolean = false

    /**
     * Visits a JSON object.
     *
     * @param entry The JSON entry to visit.
     * @return true if the visitor should skip to the next entry, false otherwise.
     *
     */
    fun visit(entry: Map.Entry<String, JsonValue>): Boolean = false

    /**
     * Visits a JSON array.
     *
     * @param obj The JSON array to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    fun visit(obj: JsonObjectBase): Boolean = false

    /**
     * Visits a JSON array.
     *
     * @param array The JSON array to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    fun visit(array: JsonArrayBase): Boolean = false
}


