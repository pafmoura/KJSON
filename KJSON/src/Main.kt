import iscte.main.kjson.JsonBoolean
import iscte.main.kjson.JsonNull
import iscte.main.kjson.JsonNumber
import iscte.main.kjson.JsonObject
import iscte.main.kjson.JsonString
import iscte.main.kjson.MutableJsonObject

fun main() {
    val obj : MutableJsonObject = MutableJsonObject(mutableMapOf(
        "unidade curricular" to JsonString("PA"),
        "nota" to JsonNumber(20),
        "aprovado" to JsonBoolean(true),
        "data de entrega" to JsonNull,
        "professor" to JsonObject(mapOf(
            "nome" to JsonString("André"),
            "idade" to JsonNull,
            "homem" to JsonBoolean(true),
            "gabinete" to JsonString("D6.23")
        ))
    ))
    MutableJsonObject()
}