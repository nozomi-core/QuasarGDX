package app.quasar.qgl.serialize

import app.quasar.qgl.language.Outcome
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromStream
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

private val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
fun qglDecodeJsonFile(file: File): Outcome<QGLJson, Unit> {
    return try {
        val fileInput = BufferedInputStream(FileInputStream(file))
        val decode: JsonElement = json.decodeFromStream(fileInput)
        Outcome.Success(QGLJson.fromJsonElement(decode))
    } catch (e: Exception) {
        Outcome.Failed(e, Unit)
    }
}