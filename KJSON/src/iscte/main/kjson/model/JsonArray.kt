package iscte.main.kjson.model

abstract class JsonArrayBase(
    val list: List<JsonValue> = listOf<JsonValue>()
) : JsonValue, List<JsonValue> {

    override val data: List<JsonValue> get() = list

    override fun toJsonString(): String {
        return data.joinToString(
            separator = ", ", prefix = "[", postfix = "]"
        ) { it.toJsonString() }
    }


    fun isAllSameType(): Boolean {
        val visitor = VisitorAllSameType()
        this.accept(visitor)
        return visitor.isValid()
    }

    fun filter(
        valuePredicate: (JsonValue) -> Boolean,
        keyPredicate: (String) -> Boolean
    ): JsonArrayBase {
        val visitor = VisitorFilterArray(valuePredicate, keyPredicate)
        accept(visitor)
        return visitor.getResult()
    }

    fun map(
        valueAction: (JsonValue) -> JsonValue,
        keyAction: (String) -> String
    ): JsonArrayBase {
        val visitor = VisitorMapArray(valueAction, keyAction)
        accept(visitor)
        return visitor.getResult()
    }


}


class JsonArray(
    list: List<JsonValue>
) : JsonArrayBase(list), List<JsonValue> by list {
    operator fun plus(other: JsonArray): JsonArray {
        return JsonArray(this.data + other.data)
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): JsonArray {
        return JsonArray(this.data.subList(fromIndex, toIndex))
    }

    override fun equals(other: Any?): Boolean {
        require(other is JsonArray || other is MutableJsonArray)
        return data == other.data


    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    /*
    fun map(action: (JsonValue) -> JsonValue): JsonArray {

        val mappedList = mutableListOf<JsonValue>()
        this.accept { value ->
            mappedList.add(action(value))
        }
        return JsonArray(mappedList)

    }
    */

//    fun map(action: (JsonValue) -> JsonValue) : JsonArray{
//        return map(valueAction = action)
//    }

//    fun map(
//        valueAction: (JsonValue) -> JsonValue,
//        keyAction: ((String) -> String) = {k->k}
//    ): JsonArray {
//        val mappedList = mutableListOf<JsonValue>()
//        this.accept { value ->
//            when (value) {
//                is JsonArray -> {
//                    mappedList.add(value.map(valueAction, keyAction))
//                }
//                is JsonObjectBase -> {
//                    val mappedProperties = value.mapValues { (k, v) ->
//                        val newKey = keyAction(k)
//                        val newValue = valueAction(v)
//                        Pair(newKey, newValue)
//                    }.map { it.value }.toMap()
//                    mappedList.add(JsonObject(mappedProperties))
//                }
//                else -> {
//                    mappedList.add(valueAction(value))
//                }
//            }
//        }
//        return JsonArray(mappedList)
//    }


//    fun filter(predicate: (JsonValue) -> Boolean, keyPredicate: (String) -> Boolean = { key -> true } ): JsonArray {
//        val filteredList = mutableListOf<JsonValue>()
//        this.accept {
//
//            value ->
//            print(value.toJsonString())
//            when (value) {
//                is MutableJsonObject -> { //TEMOS DE FAZER CLASSE ABSTRATA
//
//                    var filtered = value.filter(predicate,keyPredicate)
//                    if (filtered.isNotEmpty()) {
//                        filteredList.add(filtered)
//                    }
//                }
//
//                is JsonArray -> {
//                    var filteredArr = MutableJsonArray(mutableListOf())
//                    value.accept { entry ->
//                        if (predicate(entry)) {
//                            filteredArr.add(entry)
//                        }
//                    }
//                    if (filteredArr.isNotEmpty()) {
//                        filteredList.add(JsonArray(filteredArr))
//                    }
//                }
//
//                else -> {
//                    if (predicate(value)) {
//                        filteredList.add(value)
//                    }
//                }
//            }
//        }
//        return JsonArray(filteredList)
//    }


}

class MutableJsonArray(
    list: MutableList<JsonValue>
) : JsonArrayBase(list), MutableList<JsonValue> by list {

    operator fun plus(other: MutableJsonArray): MutableJsonArray {
        return MutableJsonArray((list + other.list).toMutableList())
    }

    override fun equals(other: Any?): Boolean {
        require(other is JsonArray || other is MutableJsonArray)
        return data == other.data


    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}


//        fun map(action: (JsonValue) -> JsonValue) : MutableJsonArray{
//            return map(valueAction = action)
//        }
//
//        fun map(
//            valueAction: (JsonValue) -> JsonValue,
//            keyAction: ((String) -> String) = {k->k}
//        ): MutableJsonArray {
//            val mappedList = mutableListOf<JsonValue>()
//            this.accept { value ->
//                when (value) {
//                    is JsonArray -> {
//                        mappedList.add(value.map(valueAction, keyAction))
//                    }
//                    is JsonObjectBase -> {
//                        val mappedProperties = value.mapValues { (k, v) ->
//                            val newKey = keyAction(k)
//                            val newValue = valueAction(v)
//                            Pair(newKey, newValue)
//                        }.map { it.value }.toMap()
//                        mappedList.add(JsonObject(mappedProperties))
//                    }
//                    else -> {
//                        mappedList.add(valueAction(value))
//                    }
//                }
//            }
//            return MutableJsonArray(mappedList)
//        }
//    }

