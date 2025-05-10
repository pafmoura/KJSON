package iscte.main.kjson.visitor

import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue
import iscte.main.kjson.model.MutableJsonArray
import iscte.main.kjson.model.MutableJsonObject


/**
 * Visitor that filters JSON objects and arrays based on given predicates.
 *
 * This visitor traverses the JSON structure and applies the provided predicates to filter elements.
 */
class VisitorFilterObject(
    private val valuePredicate: (JsonValue) -> Boolean,
    private val keyPredicate: (String) -> Boolean
) : JsonVisitor {
    val filteredMap = mutableMapOf<String, JsonValue>()

    /**
     * The first JSON object encountered during the traversal.
     * This is used to determine the type of the resulting filtered object.
     */
    private var firstObjectJson: JsonValue? = null

    /**
     * Visits a JSON entry and applies the filtering predicates.
     *
     * @param entry The JSON entry to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
        val key = entry.key
        val value = entry.value
        when (value) {
            is JsonArrayBase -> {
                val array = value.filter(valuePredicate, keyPredicate)

                if (array.isNotEmpty()) {
                    filteredMap.put(key, array)
                    return true
                }
            }

            is JsonObjectBase -> {
                val obj = value.filter(valuePredicate, keyPredicate)

                if (obj.isNotEmpty()) {
                    filteredMap.put(key, obj)
                    return true
                }
            }

            else -> {
                if (valuePredicate(value) && keyPredicate(key)) {
                    filteredMap.put(key, value)
                }
            }
        }
        return false
    }

    /**
     * Visits a JSON object and applies the filtering predicates.
     *
     * @param obj The JSON object to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(obj: JsonObjectBase): Boolean {
        firstObjectJson = firstObjectJson ?: obj

        return false
    }

    /**
     * Visits a JSON array and applies the filtering predicates.
     *
     * @param array The JSON array to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(array: JsonArrayBase): Boolean {
        return true
    }

    /**
     * Returns the filtered JSON object based on the first encountered JSON object type.
     *
     * @return The filtered JSON object.
     * @throws IllegalStateException if the first encountered JSON object type is unknown.
     */
    fun getResult(): JsonObjectBase {
        return when (firstObjectJson) {
            is MutableJsonObject -> MutableJsonObject(filteredMap)
            is JsonObject -> JsonObject(filteredMap)
            else -> throw IllegalStateException("Unknown JsonObjectBase type")
        }
    }
}

/**
 * Visitor that filters JSON arrays based on given predicates.
 *
 * This visitor traverses the JSON structure and applies the provided predicates to filter elements.
 */
class VisitorFilterArray(
    private val valuePredicate: (JsonValue) -> Boolean,
    private val keyPredicate: (String) -> Boolean
) : JsonVisitor {
    val filteredList = mutableListOf<JsonValue>()
    private var firstJsonArrayBase: JsonArrayBase? = null

    /**
     * Visits a JSON value and applies the filtering predicates.
     *
     * @param value The JSON value to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(value: JsonValue): Boolean {
        if (valuePredicate(value)) {
            filteredList.add(value)
        }
        return false
    }


    /**
     * Visits a JSON object and applies the filtering predicates.
     *
     * @param obj The JSON object to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(obj: JsonObjectBase): Boolean {
        val filteredObj = obj.filter(valuePredicate, keyPredicate)

        if (filteredObj.isNotEmpty()) {
            filteredList.add(filteredObj)
        }

        return true
    }


    /**
     * Visits a JSON array and applies the filtering predicates.
     *
     * @param array The JSON array to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(array: JsonArrayBase): Boolean {
        if (firstJsonArrayBase == null){
            firstJsonArrayBase = array
            return false
        }

        val filteredArray = array.filter(valuePredicate, keyPredicate)

        if (filteredArray.isNotEmpty()) {
            filteredList.add(filteredArray)
        }

        return true
    }

    /**
     * Returns the filtered JSON array based on the first encountered JSON array type.
     *
     * @return The filtered JSON array.
     * @throws IllegalStateException if the first encountered JSON array type is unknown.
     */
    fun getResult(): JsonArrayBase {
        return when (firstJsonArrayBase) {
            is JsonArray -> JsonArray(filteredList)
            is MutableJsonArray -> MutableJsonArray(filteredList)
            else -> throw IllegalStateException("Unknown JsonArrayBase type")
        }
    }
}