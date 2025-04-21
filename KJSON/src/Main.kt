import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonBoolean
import iscte.main.kjson.model.JsonNull
import iscte.main.kjson.model.JsonNumber
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonString
import iscte.main.kjson.model.MutableJsonObject
import org.junit.jupiter.api.Assertions.assertFalse

fun main() {
    val jsonArray2 = JsonArray(
        listOf(
            JsonString("Hello"),
            JsonNumber(1),
            JsonBoolean(false)
        )
    )
    print(jsonArray2.isAllSameType())
}