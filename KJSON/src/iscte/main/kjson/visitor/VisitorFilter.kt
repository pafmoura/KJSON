package iscte.main.kjson.visitor

import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonArrayBase
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonObjectBase
import iscte.main.kjson.model.JsonValue
import iscte.main.kjson.model.MutableJsonArray
import iscte.main.kjson.model.MutableJsonObject


class VisitorFilterObject(
    private val valuePredicate: (JsonValue) -> Boolean,
    private val keyPredicate: (String) -> Boolean
) : JsonVisitor {
    val filteredMap = mutableMapOf<String, JsonValue>()

    private var firstObjectJson: JsonValue? = null

    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
        val key = entry.key
        val value = entry.value
        when (value) {
            is JsonArrayBase -> {
                val array = value.filter(valuePredicate, keyPredicate)

                if (array.isNotEmpty()) {
                    filteredMap.put(key, array)

                }
            }

            is JsonObjectBase -> {
                val obj = value.filter(valuePredicate, keyPredicate)

                if (obj.isNotEmpty()) {
                    filteredMap.put(key, obj)

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

    override fun visit(obj: JsonObjectBase): Boolean {
        firstObjectJson = firstObjectJson ?: obj
        return false
    }

    override fun visit(array: JsonArrayBase): Boolean {
        return true
    }

    fun getResult(): JsonObjectBase {
        return when (firstObjectJson) {
            is MutableJsonObject -> MutableJsonObject(filteredMap)
            is JsonObject -> JsonObject(filteredMap)
            else -> throw IllegalStateException("Unknown JsonObjectBase type")
        }
    }
}

class VisitorFilterArray(
    private val valuePredicate: (JsonValue) -> Boolean,
    private val keyPredicate: (String) -> Boolean
) : JsonVisitor {
    val filteredList = mutableListOf<JsonValue>()
    private var firstJsonArrayBase: JsonArrayBase? = null

    override fun visit(value: JsonValue): Boolean {
        if (valuePredicate(value)) {
            filteredList.add(value)
        }
        return false
    }


    override fun visit(obj: JsonObjectBase): Boolean {
        val filteredObj = obj.filter(valuePredicate, keyPredicate)

        if (filteredObj.isNotEmpty()) {
            filteredList.add(filteredObj)
        }

        return true
    }


    override fun visit(array: JsonArrayBase): Boolean {
        firstJsonArrayBase = firstJsonArrayBase ?: array
        return false
    }

    fun getResult(): JsonArrayBase {
        return when (firstJsonArrayBase) {
            is JsonArray -> JsonArray(filteredList)
            is MutableJsonArray -> MutableJsonArray(filteredList)
            else -> throw IllegalStateException("Unknown JsonArrayBase type")
        }
    }
}