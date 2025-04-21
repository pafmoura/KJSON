package iscte.main.kjson.model

data class JsonNumber(override val data: Number) : JsonValue {
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

    //To check
    operator fun unaryPlus(): JsonNumber {
        return JsonNumber(this.data)
    }


    //To check
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

    override fun equals(other: Any?): Boolean {
        require(other is JsonNumber)
        return when {
            this.data is Int && other.data is Int -> this.data == other.data
            this.data is Long && other.data is Long -> this.data == other.data
            this.data is Double && other.data is Double -> this.data == other.data
            else -> this.data.toDouble() == other.data.toDouble()
        }

    }

    override fun hashCode(): Int {
        return when (this.data) {
            is Int -> this.data.hashCode()
            is Long -> this.data.hashCode()
            is Double -> this.data.hashCode()
            else -> throw IllegalArgumentException("Invalid type for hash code")
        }
    }

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

    operator fun compareTo(other: JsonNumber): Int {
        return when {
            this.data is Int && other.data is Int -> this.data.compareTo(other.data)
            this.data is Long && other.data is Long -> this.data.compareTo(other.data)
            this.data is Double && other.data is Double -> this.data.compareTo(other.data)
            else -> this.data.toDouble().compareTo(other.data.toDouble())
        }
    }
}