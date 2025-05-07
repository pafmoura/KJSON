package iscte.main.web

import iscte.main.kjson.utils.JsonReflection
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
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

    fun pageNotFound(): String {
        return """
            <html>
                <head>
                    <title>Page Not Found</title>
                </head>
                <body>
                    <h1>404 - Page Not Found</h1>
                    <p>The requested URL was not found on this server.</p>
                </body>
            </html>
        """.trimIndent()
    }

    fun transformURL(request: RecordedRequest, path: String): String {
        val (requestRoot, params) = request.path.toString().split("?")
        val requestParams = params.split("&")
        val keyParams = path.split("?", limit = 2)[1].split("&")

        var newParams = keyParams.joinToString(separator = "&") { keyParam ->
            requestParams.find { requestParam ->
                requestParam.contains(keyParam.split("=")[0])
            }.toString()
        }
        return "$requestRoot?$newParams"
    }

    fun getPathParams(match: MatchResult, path: String): Pair<KFunction<*>, Map<String, String>> {
        val kFunction = pathing[path]!!
        val groupNames: List<String> = kFunction.parameters.mapNotNull { it.name }
        val pathParams = groupNames.associateWith { name ->
            match.groups[name]?.value!!
        }
        return Pair(kFunction, pathParams)
    }

    fun getFunctionAndParams(request: RecordedRequest): Pair<KFunction<*>, Map<String, String>> {
        var kFunction: KFunction<*>? = ::pageNotFound
        var pathParams: Map<String, String> = mapOf()
        pathing.entries.forEach {

            val pattern = Regex(it.key)
            var requestedURL = request.path.toString()

            if (requestedURL.contains("?") && it.key.contains("?")) {
                requestedURL = transformURL(request, it.key)
            }

            val match = pattern.matchEntire(requestedURL)
            if (match != null) {
                val pathpair = getPathParams(match, it.key)
                kFunction = pathpair.first
                pathParams = pathpair.second
            }
        }
        return Pair(kFunction!!, pathParams)
    }

    fun initDispatcher() {

        server.dispatcher = object : okhttp3.mockwebserver.Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {

                val (kFunction, pathParams) = getFunctionAndParams(request)

                val clazz = kFunction.instanceParameter?.type?.classifier as? KClass<*>

                if (kFunction == ::welcome || kFunction == ::pageNotFound) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setHeader("Content-Type", "text/html")
                        .setBody(execute(pathParams, kFunction, null) as String)

                }
                return MockResponse().setResponseCode(200).setBody(
                    JsonReflection.toJsonValue(execute(pathParams, kFunction, clazz?.createInstance())).toJsonString()
                )
            }
        }
    }

    init {
        args.forEach { addClass(it) }
        initDispatcher()
    }

    fun welcome(): String {
        val paths = pathing.keys
            .filter { it != "/" }
            .sorted()
            .map { path ->
                path.replace(Regex("\\([^)]*\\)"), "{INPUT}")
                    .replace(Regex("\\\\"), "")
            }
        return getWelcomePageHtml(paths)
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

            val argsPaths = it.parameters.filter { it.findAnnotation<Path>() != null }
            val argsParams = it.parameters.filter { it.findAnnotation<Param>() != null }

            argsPaths.forEach { p ->
                val paramName = p.name
                if (mapping.path.contains("/{${paramName}}") == true)
                    path = path.replace("/{${paramName}}", "/(?<${paramName}>[^/]+)")
            }

            val query = argsParams.takeIf { it.isNotEmpty() }
                ?.joinToString(separator = "&", prefix = "\\?") {
                    "${it.name}=(?<${it.name}>[^/]+)"
                }
                ?: ""

            path += query

            pathing.put(path, it)
        }
    }


    private fun <T> execute(args: Map<String, Any>, kfunction: KFunction<*>, instance: T): Any? {
        val callArgs = mutableMapOf<KParameter, Any?>()

        kfunction.parameters.forEach { param ->
            when (param.kind) {
                KParameter.Kind.INSTANCE -> callArgs[param] = instance
                KParameter.Kind.VALUE -> callArgs[param] = convert(args[param.name].toString(), param.type)
                else -> {}
            }
        }
        return kfunction.callBy(callArgs)
    }

    private fun convert(value: String, type: KType): Any = when (type.classifier) {
        Int::class -> value.toInt()
        Long::class -> value.toLong()
        Double::class -> value.toDouble()
        Boolean::class -> value.toBooleanStrictOrNull() ?: false
        String::class -> value
        else -> throw IllegalArgumentException("Unsupported type: $type")
    }
}