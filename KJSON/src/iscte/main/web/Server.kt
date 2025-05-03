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


fun main() {
//    val c = Controller()
//    println(c.args(text = "OLA", n = 1))
    val app = GetJson(Controller::class)
    app.start()
}
