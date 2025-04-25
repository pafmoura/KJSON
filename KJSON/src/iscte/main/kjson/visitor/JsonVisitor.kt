package iscte.main.kjson.visitor

import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue

interface JsonVisitor {
    fun visit(value: JsonValue): Boolean = false
    fun visit(entry: Map.Entry<String, JsonValue>): Boolean = false
    fun visit(obj: JsonObjectBase): Boolean = false
    fun visit(array: JsonArrayBase): Boolean = false
}


