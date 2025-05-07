import iscte.main.kjson.utils.JsonReflection
import iscte.main.web.GetJson
import iscte.main.web.Mapping
import iscte.main.web.Param
import iscte.main.web.Path


@Mapping("api")
class Controller {
    @Mapping("ints")
    fun demo(): List<Int> = listOf(1, 2, 3)

    @Mapping("pair")
    fun obj(): Pair<String, String> = Pair("um", "dois")

    @Mapping("path/{pathvar}")
    fun path(
        @Path pathvar: String
    ): String = "$pathvar!"

    @Mapping("args")
    fun args(
        @Param n: Int,
        @Param text: String
    ): Map<String, String> = mapOf(text to text.repeat(n))
}

@Mapping("utils")
class ControllerTwo {

    @Mapping("greet/{name}")
    fun greet(
        @Path name: String
    ): String = "Hello, $name!"

    @Mapping("calculate/{a}/{b}")
    fun calculate(
        @Path a: Int,
        @Path b: Int,
        @Param op: String
    ): String {
        return when (op) {
            "add" -> (a + b).toString()
            "sub" -> (a - b).toString()
            "mul" -> (a * b).toString()
            "div" -> (a / b).toString()
            else -> "Invalid operation"
        }
    }


}


fun main() {
    val app = GetJson(Controller::class, ControllerTwo::class)
    app.start()

}
