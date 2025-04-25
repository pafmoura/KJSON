package iscte.main.kjson.model

import kotlin.reflect.KClass

interface JsonVisitor {
    fun visit(value: JsonValue): Boolean = false
    fun visit(entry: Map.Entry<String, JsonValue>): Boolean = false
    fun visit(obj: JsonObjectBase): Boolean = false
    fun visit(array: JsonArrayBase): Boolean = false
}


class VisitorValidObject : JsonVisitor {
    private val keys = mutableSetOf<String>()
    private var isValid = true

    override fun visit(entry: Map.Entry<String, JsonValue>): Boolean {
        isValid = isValid && keys.add(entry.key)
        return false
    }

    fun isValid() = isValid
}

class VisitorAllSameType : JsonVisitor {
    private var firstElementClass: KClass<out JsonValue>? = null
    private var isValid = true

    override fun visit(value: JsonValue): Boolean {
        if (firstElementClass == null)
            firstElementClass = value::class

        isValid = isValid && value::class == firstElementClass && value !is JsonNull
        return false
    }

    override fun visit(obj: JsonObjectBase): Boolean {
        if (firstElementClass == null)
            firstElementClass = obj::class

        isValid = isValid && obj::class == firstElementClass
        return false
    }

    override fun visit(array: JsonArrayBase): Boolean {
        if (firstElementClass == null)
            firstElementClass = array::class

        isValid = isValid && array::class == firstElementClass
        return false
    }

    fun isValid() = isValid
}


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
                val array = (value as JsonArrayBase).filter(valuePredicate, keyPredicate)

                if (array.isNotEmpty() && key !in filteredMap) {
                    filteredMap.put(key, array)
                }
            }

            is JsonObjectBase -> {
                val obj = (value as JsonObjectBase).filter(valuePredicate, keyPredicate)

                if (obj.isNotEmpty() && key !in filteredMap) {
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
                val array = (value as JsonArrayBase).map(valueAction, keyAction)
                val newKey = keyAction(key)
                if (array.isNotEmpty() && newKey !in newMap) {
                    newMap.put(newKey, array)
                }
            }

            is JsonObjectBase -> {
                val obj = (value as JsonObjectBase).map(valueAction, keyAction)
                val newKey = keyAction(key)
                if (obj.isNotEmpty() && newKey !in newMap) {
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