package kjson.model

/**
 * Represents a JSON number value.
 *
 * @property data The number value.
 *
 * This class implements the [JsonValue] interface, which means it can be used in JSON structures.
 */
data class JsonNumber(override val data: Number) : JsonValue {

    /**
     * Overrides operator for arithmetic plus operation.
     *
     * @param other The other [JsonNumber] to add.
     *
     * @return A new [JsonNumber] representing the sum of this number and the other number.
     *
     * This operator function allows you to use the `+` operator to add two [JsonNumber] instances together.
     */
    operator fun plus(other: JsonNumber): JsonNumber {
        return JsonNumber(
            when {
                this.data is Int && other.data is Int -> this.data + other.data
                this.data is Long && other.data is Long -> this.data + other.data
                this.data is Double && other.data is Double -> this.data + other.data
                else -> this.data.toDouble() + other.data.toDouble()
            }
        )
    }

    /**
     * Overrides operator for arithmetic minus operation.
     *
     * @param other The other [JsonNumber] to subtract.
     *
     * @return A new [JsonNumber] representing the difference of this number and the other number.
     *
     * This operator function allows you to use the `-` operator to subtract two [JsonNumber] instances.
     */
    operator fun minus(other: JsonNumber): JsonNumber {
        return JsonNumber(
            when {
                this.data is Int && other.data is Int -> this.data - other.data
                this.data is Long && other.data is Long -> this.data - other.data
                this.data is Double && other.data is Double -> this.data - other.data
                else -> this.data.toDouble() - other.data.toDouble()
            }
        )
    }

    /**
     * Overrides operator for arithmetic multiplication operation.
     *
     * @param other The other [JsonNumber] to multiply.
     *
     * @return A new [JsonNumber] representing the product of this number and the other number.
     *
     * This operator function allows you to use the `*` operator to multiply two [JsonNumber] instances together.
     */
    operator fun times(other: JsonNumber): JsonNumber {
        return JsonNumber(
            when {
                this.data is Int && other.data is Int -> this.data * other.data
                this.data is Long && other.data is Long -> this.data * other.data
                this.data is Double && other.data is Double -> this.data * other.data
                else -> this.data.toDouble() * other.data.toDouble()
            }
        )


    }

    /**
     * Overrides operator for arithmetic division operation.
     *
     * @param other The other [JsonNumber] to divide.
     *
     * @return A new [JsonNumber] representing the quotient of this number and the other number.
     *
     * This operator function allows you to use the `/` operator to divide two [JsonNumber] instances.
     */
    operator fun div(other: JsonNumber): JsonNumber {
        return JsonNumber(
            when {
                this.data is Int && other.data is Int -> this.data / other.data
                this.data is Long && other.data is Long -> this.data / other.data
                this.data is Double && other.data is Double -> this.data / other.data
                else -> this.data.toDouble() / other.data.toDouble()
            }
        )
    }

    /**
     * Overrides operator for arithmetic modulo operation.
     *
     * @param other The other [JsonNumber] to calculate the modulus.
     *
     * @return A new [JsonNumber] representing the modulus of this number and the other number.
     *
     * This operator function allows you to use the `%` operator to calculate the modulus of two [JsonNumber] instances.
     */
    operator fun rem(other: JsonNumber): JsonNumber {
        return JsonNumber(
            when {
                this.data is Int && other.data is Int -> this.data % other.data
                this.data is Long && other.data is Long -> this.data % other.data
                this.data is Double && other.data is Double -> this.data % other.data
                else -> this.data.toDouble() % other.data.toDouble()
            }
        )
    }

    /**
     * Overrides operator for unary plus operation.
     *
     * @return A new [JsonNumber] representing the same value as this number.
     *
     * This operator function allows you to use the `+` operator to indicate a positive number.
     */
    operator fun unaryPlus(): JsonNumber {
        return JsonNumber(this.data)
    }


    /**
     * Overrides operator for unary minus operation.
     *
     * @return A new [JsonNumber] representing the negation of this number.
     *
     * This operator function allows you to use the `-` operator to negate a [JsonNumber] instance.
     */
    operator fun unaryMinus(): JsonNumber {
        return JsonNumber(
            when (this.data) {
                is Int -> -this.data
                is Long -> -this.data
                is Double -> -this.data
                else -> throw IllegalArgumentException("Invalid type for unary minus")
            }
        )
    }

    /**
     * Overrides the equals function to compare two [JsonNumber] instances.
     *
     * @param other The other object to compare with.
     *
     * @return True if the two [JsonNumber] instances are equal, false otherwise.
     *
     * This function checks if the other object is also a [JsonNumber] and compares their values.
     * The implementation ensures that the comparison is done in a type-safe manner, taking into account
     * the possible types of the number (Int, Long, Double).
     */
    override fun equals(other: Any?): Boolean {
        require(other is JsonNumber)
        return when {
            this.data is Int && other.data is Int -> this.data == other.data
            this.data is Long && other.data is Long -> this.data == other.data
            this.data is Double && other.data is Double -> this.data == other.data
            else -> this.data.toDouble() == other.data.toDouble()
        }

    }

    /**
     * Overrides the hashCode function to generate a hash code for this [JsonNumber].
     *
     * @return The hash code of this [JsonNumber].
     *
     * This function generates a hash code based on the type and value of the number.
     */
    override fun hashCode(): Int {
        return when (this.data) {
            is Int -> this.data.hashCode()
            is Long -> this.data.hashCode()
            is Double -> this.data.hashCode()
            else -> throw IllegalArgumentException("Invalid type for hash code")
        }
    }

    /**
     * Overrides the increment operation to increase the value of this [JsonNumber] by 1.
     *
     * @return A new [JsonNumber] representing the incremented value.
     *
     * This operator function allows you to use the `++` operator to increment a [JsonNumber] instance.
     * It handles different numeric types (Int, Long, Double) and returns a new instance with the incremented value.
     */
    operator fun inc(): JsonNumber {
        return JsonNumber(
            when (this.data) {
                is Int -> this.data + 1
                is Long -> this.data + 1
                is Double -> this.data + 1
                else -> throw IllegalArgumentException("Invalid type for increment")
            }
        )
    }

    /**
     * Overrides the decrement operation to decrease the value of this [JsonNumber] by 1.
     *
     * @return A new [JsonNumber] representing the decremented value.
     *
     * This operator function allows you to use the `--` operator to decrement a [JsonNumber] instance.
     * It handles different numeric types (Int, Long, Double) and returns a new instance with the decremented value.
     */
    operator fun dec(): JsonNumber {
        return JsonNumber(
            when (this.data) {
                is Int -> this.data - 1
                is Long -> this.data - 1
                is Double -> this.data - 1
                else -> throw IllegalArgumentException("Invalid type for decrement")
            }
        )
    }


    /**
     * Overrides the compareTo function to compare two [JsonNumber] instances.
     *
     * @param other The other [JsonNumber] to compare with.
     *
     * @return An integer representing the comparison result:
     *         - Negative if this number is less than the other number
     *         - Zero if they are equal
     *         - Positive if this number is greater than the other number
     *
     * This function compares the values of two [JsonNumber] instances, taking into account their types (Int, Long, Double).
     */
    operator fun compareTo(other: JsonNumber): Int {
        return when {
            this.data is Int && other.data is Int -> this.data.compareTo(other.data)
            this.data is Long && other.data is Long -> this.data.compareTo(other.data)
            this.data is Double && other.data is Double -> this.data.compareTo(other.data)
            else -> this.data.toDouble().compareTo(other.data.toDouble())
        }
    }
}