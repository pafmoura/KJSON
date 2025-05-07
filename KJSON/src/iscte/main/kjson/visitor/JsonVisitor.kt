package iscte.main.kjson.visitor

import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue

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
    fun visit(value: JsonValue): Boolean = false
    fun visit(entry: Map.Entry<String, JsonValue>): Boolean = false
    fun visit(obj: JsonObjectBase): Boolean = false
    fun visit(array: JsonArrayBase): Boolean = false
}


