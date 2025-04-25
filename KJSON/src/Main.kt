import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonBoolean
import iscte.main.kjson.model.JsonNull
import iscte.main.kjson.model.JsonNumber
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonString
import iscte.main.kjson.model.MutableJsonObject
import iscte.main.kjson.model.VisitorAllSameType
import org.junit.jupiter.api.Assertions.assertFalse

fun main() {
    val extra = MutableJsonObject(
        mutableMapOf(
            "unidade curricular" to JsonString("PA"),
            "Alunos" to JsonArray(listOf(JsonString("Paulo"), JsonString("Filipe"))),
            "Dias" to JsonArray(listOf(JsonNumber(2), JsonNumber(5), JsonNumber(7)))
        )
    )
    val extra1 = MutableJsonObject(
        mutableMapOf(
            "unidade curricular" to JsonString("PA"),
            "Alunos" to JsonArray(listOf(JsonString("Paulo"), extra)),
            "Dias" to JsonArray(listOf(JsonNumber(3), JsonNumber(6), JsonNumber(8)))
        )
    )
    val obj1 = MutableJsonObject(
        mutableMapOf(
            "unidade curricular" to JsonString("PA"),
            "Alunos" to JsonArray(listOf(JsonString("Paulo"), extra)),
            "Dias" to JsonArray(listOf(JsonNumber(4), JsonNumber(7), JsonNumber(9))),
            "BEJING" to extra
        ))

    val arr = JsonArray(listOf(JsonString("Paulo"), extra))
    val arr1 = JsonArray(listOf(JsonString("Paulo"), extra1))
    println(arr1.filter(
        valuePredicate = { it -> it.data == "PA" },
        keyPredicate = {key -> key == "unidade curricular"}
    ).toJsonString())
    println(arr1.toJsonString())
    println(obj1.map(
        valueAction = {it -> JsonString(it.data.toString())},
    ).toJsonString())
}