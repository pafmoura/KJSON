import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonNumber
import iscte.main.kjson.model.JsonString
import iscte.main.kjson.model.MutableJsonObject
import iscte.main.kjson.utils.JsonReflection


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
    val extra2 = MutableJsonObject(
        mutableMapOf(
            "" to JsonString("2"),
            "4" to JsonString("2"),
        )
    )
    val obj1 = MutableJsonObject(
        mutableMapOf(
            "unidade curricular" to JsonString("PA"),
            "Alunos" to JsonArray(listOf(JsonString("Paulo"), extra)),
            "Dias" to JsonArray(listOf(JsonNumber(4), JsonNumber(7), JsonNumber(9))),
            "BEJING" to extra
        )
    )

    val obj2 = MutableJsonObject(
        mutableMapOf(
            "" to JsonString("1"),
            "4" to JsonString("2"),
            "1" to extra2,

            )
    )


    val arr = JsonArray(listOf(JsonString("Paulo"), extra))
    val arr1 = JsonArray(listOf(JsonString("Paulo"), extra1))

    println(
        arr.filter(
            valuePredicate = { it -> it.data == "Paulo" },
            keyPredicate = { key -> key == "Alunos" }
        ).toJsonString())
    println(arr.toJsonString())
    println(
        obj1.map(
            valueAction = { it -> JsonString(it.data.toString()) },
        ).toJsonString()
    )



    print(obj2.toJsonString())


}


data class Course(
    val name: String,
    val credits: Int,
    val evaluation: List<EvalItem>
)


data class EvalItem(
    val name: String,
    val percentage: Double,
    val mandatory: Boolean,
    val type: EvalType?
) {

}


enum class EvalType {
    TEST, PROJECT, EXAM
}

