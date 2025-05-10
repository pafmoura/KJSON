package kjson.visitor

import kjson.model.JsonArray
import kjson.model.JsonArrayBase
import kjson.model.JsonObject
import kjson.model.JsonObjectBase
import kjson.model.JsonValue
import kjson.model.MutableJsonArray
import kjson.model.MutableJsonObject


/**
 * Visitor that maps JSON objects and arrays based on given actions.
 *
 * This visitor traverses the JSON structure and applies the provided actions to transform elements.
 */
class VisitorMapObject(
    private val valueAction: (JsonValue) -> JsonValue,
    private val keyAction: (String) -> String
) : JsonVisitor {
    val newMap = mutableMapOf<String, JsonValue>()

    /**
     * The first JSON object encountered during the traversal.
     * This is used to determine the type of the resulting mapped object.
     */
    private var firstObjectJson: JsonValue? = null

    /**
     * Visits a JSON entry and applies the mapping actions.
     *
     * @param entry The JSON entry to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
        val key = entry.key
        val value = entry.value
        when (value) {
            is JsonArrayBase -> {
                val array = value.map(valueAction, keyAction)
                val newKey = keyAction(key)
                if (array.isNotEmpty()) {
                    newMap.put(newKey, array)
                    return false
                }
            }

            is JsonObjectBase -> {
                val obj = value.map(valueAction, keyAction)
                val newKey = keyAction(key)
                if (obj.isNotEmpty()) {
                    newMap.put(newKey, obj)
                    return true
                }
            }

            else -> {
                newMap.put(keyAction(key), valueAction(value))
            }
        }
        return false
    }

    /**
     * Visits a JSON object and applies the mapping actions.
     *
     * @param obj The JSON object to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(obj: JsonObjectBase): Boolean {
        firstObjectJson = firstObjectJson ?: obj
        return false
    }

    /**
     * Visits a JSON array and applies the mapping actions.
     *
     * @param array The JSON array to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(array: JsonArrayBase): Boolean {
        return true
    }

    /**
     * Returns the resulting mapped JSON object based on the first encountered JSON object type.
     *
     * @return The resulting mapped JSON object.
     * @throws IllegalStateException if the type of the first encountered JSON object is unknown.
     */
    fun getResult(): JsonObjectBase {
        return when (firstObjectJson) {
            is MutableJsonObject -> MutableJsonObject(newMap)
            is JsonObject -> JsonObject(newMap)
            else -> throw IllegalStateException("Unknown JsonObjectBase type")
        }
    }
}

/**
 * Visitor that maps JSON arrays based on given actions.
 *
 * This visitor traverses the JSON structure and applies the provided actions to transform elements.
 */
class VisitorMapArray(
    private val valueAction: (JsonValue) -> JsonValue,
    private val keyAction: (String) -> String
) : JsonVisitor {
    val mapList = mutableListOf<JsonValue>()
    /**
     * The first JSON array encountered during the traversal.
     * This is used to determine the type of the resulting mapped array.
     */
    private var firstJsonArrayBase: JsonArrayBase? = null

    /**
     * Visits a JSON entry and applies the mapping actions.
     *
     * @param entry The JSON entry to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(value: JsonValue): Boolean {
        mapList.add(valueAction(value))
        return false
    }


    /**
     * Visits a JSON object and applies the mapping actions.
     *
     * @param obj The JSON object to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(obj: JsonObjectBase): Boolean {
        val mapObj = obj.map(valueAction, keyAction)

        if (mapObj.isNotEmpty()) {
            mapList.add(mapObj)
        }

        return true
    }


    /**
     * Visits a JSON array and applies the mapping actions.
     *
     * @param array The JSON array to visit.
     * @return true if the visitor should stop visiting, false otherwise.
     */
    override fun visit(array: JsonArrayBase): Boolean {
        if (firstJsonArrayBase == null){
            firstJsonArrayBase = array
            return false
        }

        val filteredArray = array.map(valueAction, keyAction)

        if (filteredArray.isNotEmpty()) {
            mapList.add(filteredArray)
        }

        return true
    }

    /**
     * Returns the resulting mapped JSON array based on the first encountered JSON array type.
     *
     * @return The resulting mapped JSON array.
     * @throws IllegalStateException if the type of the first encountered JSON array is unknown.
     */
    fun getResult(): JsonArrayBase {
        return when (firstJsonArrayBase) {
            is JsonArray -> JsonArray(mapList)
            is MutableJsonArray -> MutableJsonArray(mapList)
            else -> throw IllegalStateException("Unknown JsonArrayBase type")
        }
    }
}