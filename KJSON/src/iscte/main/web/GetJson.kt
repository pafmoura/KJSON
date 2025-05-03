package iscte.main.web

import iscte.main.kjson.utils.JsonReflection
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val path: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Path

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Param


class GetJson(vararg val args: KClass<*>) {
    val DEFAULT_PORT = 8080
    private val server = MockWebServer()
    private val pathing = mutableMapOf<String, KFunction<*>>(
        "/" to ::welcome
    )

    init {
        args.forEach { addClass(it) }

        server.dispatcher = object : okhttp3.mockwebserver.Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {

                var kFunction : KFunction<*>? = null
                var pathParams = mapOf<String, Any>()
                pathing.entries.forEach {
                    val pattern = Regex(it.key)
                    val match = pattern.matchEntire(request.path.toString())
                    println(pattern)
                    println(request.path.toString())
                    if (match != null) {
                        kFunction = pathing[it.key]
                        val groupNames : List<String?> = kFunction?.parameters?.mapNotNull { it.name } ?: emptyList()
                        pathParams = groupNames.associateWith { name ->
                            match.groups.get(name!!)?.value!!
                        } as Map<String, Any>
                    }
                }

                if (kFunction == null)
                    return MockResponse().setResponseCode(404).setBody("Page Not Found")

                val clazz = kFunction.instanceParameter?.type?.classifier as? KClass<*>
                return MockResponse().setResponseCode(200).setBody(
                        JsonReflection.toJsonValue(execute(pathParams ,kFunction, clazz?.createInstance())).toJsonString()
                )
            }
        }
    }

    fun welcome(): String {
        return "Welcome to PA"
    }

    fun start(port: Int = DEFAULT_PORT) {
        server.start(port)
        println("URL = ${server.url("/")}")
    }

    fun shutdown() = server.shutdown()

    fun addClass(clazz: KClass<*>) {
        val mapping = clazz.findAnnotation<Mapping>()
        require(mapping != null) { "Invalid controller class: ${clazz.simpleName}" }
        val rootPath = "/${mapping.path}"

        for (it in clazz.memberFunctions) {
            val mapping = it.findAnnotation<Mapping>()
            if (mapping == null)
                continue

            var path = "${rootPath}/${mapping.path}"

            it.parameters.forEach { p ->
                val pathAnn = p.findAnnotation<Path>()
                val paramName = p.name
                if (pathAnn != null && mapping.path.contains("/{${paramName}}") == true)
                    path = path.replace("/{${paramName}}", "/(?<${paramName}>[^/]+)")
            }
            pathing.put(path, it)
        }
    }


    private fun <T> execute(args: Map<String, Any>, kfunction: KFunction<*>, instance: T): Any? {
        val callArgs = mutableMapOf<KParameter, Any?>()

        kfunction.parameters.forEach { param ->
            when (param.kind) {
                KParameter.Kind.INSTANCE -> callArgs[param] = instance
                KParameter.Kind.VALUE -> callArgs[param] = args[param.name]
                else -> {}
            }
        }
        return kfunction.callBy(callArgs)
    }
}