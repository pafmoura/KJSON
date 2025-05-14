import kjson.model.JsonArray
import kjson.model.JsonNumber
import kjson.model.JsonObject
import kjson.model.JsonString
import kjson.model.JsonValue
import kjson.visitor.JsonVisitor

class CountStringsVisitor : JsonVisitor {
    var count = 0
        private set

    override fun visit(value: JsonValue): Boolean {
        if (value is JsonString) {
            count++
        }
        return false
    }
}
fun main(){
    val visitor = CountStringsVisitor()
    val jsonArr = JsonArray(
        JsonString("Hello"),
        JsonString("World"),
        JsonString("JSON"),
        JsonNumber(2025),
        JsonObject(
            "key1" to JsonString("value1"),
            "key2" to JsonNumber(42),
            "key3" to JsonArray(
                JsonString("item1"),
                JsonString("item2")
            )
        ))

    jsonArr.accept(visitor)
    println("Number of strings: ${visitor.count}")
}