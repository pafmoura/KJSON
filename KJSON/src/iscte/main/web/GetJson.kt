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

/**
 * Annotation to specify the mapping of a class or function to a specific path.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val path: String)

/**
 * Annotation to specify a path variable in a function parameter.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Path

/**
 * Annotation to specify a query parameter in a function parameter.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Param


/**
 * A simple HTTP server that serves JSON responses based on the provided classes and their mappings.
 *
 * @param args The classes to be used for generating JSON responses.
 */
class GetJson(vararg val args: KClass<*>) {
    val DEFAULT_PORT = 8080
    private val server = MockWebServer() // MockWebServer instance

    // Map to hold the path mappings and their corresponding functions
    private val pathing = mutableMapOf<String, KFunction<*>>(
        "/" to ::welcome
    )

    /**
     * HTML template for page not found
     * @return HTML string for 404 error page
     */
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

    /**
     * Function that transforms the URL by replacing the query parameters with the ones from the request.
     *
     * @param request The recorded request containing the original URL.
     * @param path The path to be transformed.
     * @return The transformed URL with the request parameters.
     *
     */
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

    /**
     * Function that gets path parameters based on the match result and path.
     *
     * @param match The match result of the regex pattern.
     * @param path The path to be matched.
     * @return A pair containing the KFunction to be used and a map of path parameters.
     */
    fun getPathParams(match: MatchResult, path: String): Pair<KFunction<*>, Map<String, String>> {
        val kFunction = pathing[path]!!
        val groupNames: List<String> = kFunction.parameters.mapNotNull { it.name }
        val pathParams = groupNames.associateWith { name ->
            match.groups[name]?.value!!
        }
        return Pair(kFunction, pathParams)
    }

    /**
     * Function that gets the KFunction and path parameters based on the request.
     *
     * @param request The recorded request containing the path.
     * @return A pair containing the KFunction to be used and a map of path parameters.
     */
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

    /**
     * Function get inits the server dispatcher to handle incoming requests.
     * Calls the appropriate function based on the request path and parameters.
     *
     * @return A MockResponse object containing the response to be sent back to the client.
     */
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

    /**
     * Init function that initializes the server with the provided classes.
     */
    init {
        args.forEach { addClass(it) }
        initDispatcher()
    }

    /**
     * Function that generates the welcome page HTML with the available paths.
     *
     * @return A string containing the HTML for the welcome page.
     */
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

    /**
     * Function that starts the server on the specified port.
     *
     * @param port The port on which the server will listen. Default is 8080.
     *
     * @throws IllegalArgumentException If the port is not in the valid range (1-65535).
     */
    fun start(port: Int = DEFAULT_PORT) {
        require(port in 1..65535) { "Port must be between 1 and 65535" }
        server.start(port)
        println("URL = ${server.url("/")}")
    }

    /**
     * Function that shuts down the server.
     */
    fun shutdown() = server.shutdown()

    /**
     * Function to add a class to the server.
     *
     * @param clazz The class to be added.
     *
     * @throws IllegalArgumentException If the class does not have a valid mapping.
     *
     * This function scans the class for functions with the @Mapping annotation
     * and registers them with the server.
     * It also generates the path for each function based on the mapping and
     * the parameters.
     */
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


    /**
     * Function that executes the given function with the provided arguments.
     *
     * @param args The arguments to be passed to the function.
     * @param kfunction The function to be executed.
     * @param instance The instance of the class to be used.
     *
     * @return The result of the function execution.
     */
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

    /**
     * Function to convert a string value to the specified type.
     * Mainly used for converting query parameters to their respective types.
     *
     * @param value The string value to be converted.
     * @param type The type to which the value should be converted.
     *
     * @return The converted value.
     */
    private fun convert(value: String, type: KType): Any = when (type.classifier) {
        Int::class -> value.toInt()
        Long::class -> value.toLong()
        Double::class -> value.toDouble()
        Boolean::class -> value.toBooleanStrictOrNull() ?: false
        String::class -> value
        else -> throw IllegalArgumentException("Unsupported type: $type")
    }
}