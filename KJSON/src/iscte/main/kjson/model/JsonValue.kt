package iscte.main.kjson.model

import iscte.main.kjson.visitor.JsonVisitor

/**
 * Interface that represents a JSON value.
 *
 * This interface is implemented by various classes that represent different types of JSON values,
 * such as objects, arrays, strings, numbers, booleans, and null.
 *
 * @property data The underlying data of the JSON value.
 */
interface JsonValue {

    /**
     * The underlying data of the JSON value.
     * This can be of any type, depending on the specific JSON value implementation.
     */
    val data: Any?

    /**
     * Converts the JSON value to a JSON string representation.
     *
     * @return A string representation of the JSON value in JSON format.
     */
    fun toJsonString(pretty: Boolean = false): String = this.data.toString()

    /**
     * Accepts a visitor that implements the [JsonVisitor] interface.
     *
     * This method allows the visitor to perform operations on the JSON value.
     *
     * @param visitor The visitor that will visit this JSON value.
     */
    fun accept(visitor: JsonVisitor) {
        when (this) {
            is JsonObjectBase -> {
                if (visitor.visit(this)) {
                    return
                }

                for (entry in this.data.entries){
                    if(visitor.visit(entry))
                        continue
                    entry.value.accept(visitor)
                }
            }

            is JsonArrayBase -> {
                if (visitor.visit(this)) {
                    return
                }
                this.data.forEach { value ->
                    value.accept(visitor)
                }
            }

            else -> {
                visitor.visit(this)
            }
        }
    }
}