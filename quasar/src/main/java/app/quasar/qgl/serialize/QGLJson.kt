package app.quasar.qgl.serialize

import kotlinx.serialization.json.*

/**
 * QGLJson class can take in a json tree using [JsonElement]
 * and can retrieve values using string path dot operators
 */
class QGLJson private constructor(private val jsonElement: JsonElement) {

    private fun getPathElement(path: String): JsonElement {
        val parts = path.split(".")
        var element: JsonElement? = jsonElement

        //find the json child element by splitting the path using dot operator
        parts.forEach { nextPath ->
            element = element?.jsonObject?.get(nextPath)
            if(element == null) {
                throw Exception("no path $path found in Json blob")
            }
        }
        return element!!
    }

    fun getString(path: String): String {
        val element = getPathElement(path)
        return element.jsonPrimitive.content
    }

    fun getBoolean(path: String): Boolean {
        val element = getPathElement(path)
        return element.jsonPrimitive.boolean
    }

    fun getInt(path: String): Int {
        val element = getPathElement(path)
        return element.jsonPrimitive.int
    }

    companion object {
        fun fromJsonElement(element: JsonElement): QGLJson = QGLJson(element)
    }
}