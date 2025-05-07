package iscte.tests.web

import iscte.main.kjson.model.JsonArray
import iscte.main.kjson.model.JsonNumber
import iscte.main.kjson.model.JsonObject
import iscte.main.kjson.model.JsonString
import iscte.main.web.GetJson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class GetJsonTests {
    private lateinit var server: GetJson
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        client = OkHttpClient()
        server = GetJson(Controller::class, ControllerTwo::class)
        server.start(8081)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }


    @Test
    fun testInvalidClasses() {
        assertThrows(IllegalArgumentException::class.java) {
            val app = GetJson(JsonString::class)
        }
        assertThrows(IllegalArgumentException::class.java) {
            val app = GetJson(Controller::class, JsonString::class)
        }
    }



    @Test
    fun testWelcomePage() {
        val request = Request.Builder()
            .url("http://localhost:8081/")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        assertEquals(200, response.code)
        assertTrue(responseBody?.contains("GetJson Web API") == true)


    }

    @Test
    fun testDemoInts() {
        val request = Request.Builder()
            .url("http://localhost:8081/api/ints")
            .build()

        val response = client.newCall(request).execute()
        assertEquals(200, response.code)
        assertEquals(JsonArray(listOf(JsonNumber(1),JsonNumber(2),JsonNumber(3))).toJsonString(), response.body.string())
    }

    @Test
    fun testObjPair() {
        val request = Request.Builder()
            .url("http://localhost:8081/api/pair")
            .build()

        val response = client.newCall(request).execute()
        assertEquals(200, response.code)
        assertEquals(JsonObject(mapOf("first" to JsonString("um"), "second" to JsonString("dois"))).toJsonString(), response.body.string())
    }

    @Test
    fun testPath() {
        val request = Request.Builder()
            .url("http://localhost:8081/api/path/hello")
            .build()

        val response = client.newCall(request).execute()
        assertEquals(200, response.code)
        assertEquals(JsonString("hello!").toJsonString(), response.body.string())
    }

    @Test
    fun testArgs() {
        val request = Request.Builder()
            .url("http://localhost:8081/api/args?n=3&text=hello")
            .build()

        val response = client.newCall(request).execute()
        assertEquals(200, response.code)
        assertEquals(JsonObject(mapOf("hello" to JsonString("hellohellohello"))).toJsonString(), response.body.string())
    }

    @Test
    fun testGreet() {
        val request = Request.Builder()
            .url("http://localhost:8081/utils/greet/John")
            .build()

        val response = client.newCall(request).execute()
        assertEquals(200, response.code)
        assertEquals(JsonString("Hello, John!").toJsonString(), response.body.string())
    }

    @Test
    fun testCalculate() {
        val request = Request.Builder()
            .url("http://localhost:8081/utils/calculate/10/5?op=add")
            .build()

        val response = client.newCall(request).execute()
        assertEquals(200, response.code)
        assertEquals(JsonString("15").toJsonString(), response.body.string())
    }




}