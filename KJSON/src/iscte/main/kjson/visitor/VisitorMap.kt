package iscte.main.kjson.visitor

import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue
import iscte.main.kjson.model.MutableJsonArray
import iscte.main.kjson.model.MutableJsonObject


class VisitorMapObject(
    private val valueAction: (JsonValue) -> JsonValue,
    private val keyAction: (String) -> String
) : JsonVisitor {
    val newMap = mutableMapOf<String, JsonValue>()

    private var firstObjectJson: JsonValue? = null

    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
        val key = entry.key
        val value = entry.value
        when (value) {
            is JsonArrayBase -> {
                val array = value.map(valueAction, keyAction)
                val newKey = keyAction(key)
                if (array.isNotEmpty()) {
                    newMap.put(newKey, array)
                }
            }

            is JsonObjectBase -> {
                val obj = value.map(valueAction, keyAction)
                val newKey = keyAction(key)
                if (obj.isNotEmpty()) {
                    newMap.put(newKey, obj)
                }
            }

            else -> {
                newMap.put(keyAction(key), valueAction(value))
            }
        }
        return false
    }

    override fun visit(obj: JsonObjectBase): Boolean {
        firstObjectJson = firstObjectJson ?: obj
        return false
    }

    override fun visit(array: JsonArrayBase): Boolean {
        return true
    }

    fun getResult(): JsonObjectBase {
        return when (firstObjectJson) {
            is MutableJsonObject -> MutableJsonObject(newMap)
            is JsonObject -> JsonObject(newMap)
            else -> throw IllegalStateException("Unknown JsonObjectBase type")
        }
    }
}

class VisitorMapArray(
    private val valueAction: (JsonValue) -> JsonValue,
    private val keyAction: (String) -> String
) : JsonVisitor {
    val mapList = mutableListOf<JsonValue>()
    private var firstJsonArrayBase: JsonArrayBase? = null

    override fun visit(value: JsonValue): Boolean {
        mapList.add(valueAction(value))
        return false
    }


    override fun visit(obj: JsonObjectBase): Boolean {
        val mapObj = obj.map(valueAction, keyAction)

        if (mapObj.isNotEmpty()) {
            mapList.add(mapObj)
        }

        return true
    }


    override fun visit(array: JsonArrayBase): Boolean {
        firstJsonArrayBase = firstJsonArrayBase ?: array
        return false
    }

    fun getResult(): JsonArrayBase {
        return when (firstJsonArrayBase) {
            is JsonArray -> JsonArray(mapList)
            is MutableJsonArray -> MutableJsonArray(mapList)
            else -> throw IllegalStateException("Unknown JsonArrayBase type")
        }
    }
}