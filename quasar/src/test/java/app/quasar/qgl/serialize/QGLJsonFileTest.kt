package app.quasar.qgl.serialize

import app.quasar.qgl.language.Outcome
import app.quasar.qgl.serialize.QGLJson
import app.quasar.qgl.serialize.qglDecodeJsonFile
import org.junit.Assert
import org.junit.Test
import java.io.File

class QGLJsonFileTest() {

    @Test
    fun testDecodeFile() {
        val file = File("src/test/QGLJsonFileTest.json")
        val result = qglDecodeJsonFile(file) as Outcome.Success<QGLJson, *>
        val helloWorld = result.value.getString("name")
        Assert.assertEquals("Hello world", helloWorld)
    }

    @Test
    fun testDecodeFilePath() {
        val file = File("src/test/QGLJsonFileTest.json")
        val result = qglDecodeJsonFile(file) as Outcome.Success<QGLJson, *>
        val city = result.value.getString("world.city")
        Assert.assertEquals("Melbourne", city)
    }

    @Test
    fun testDecodeBoolean() {
        val file = File("src/test/QGLJsonFileTest.json")
        val result = qglDecodeJsonFile(file) as Outcome.Success<QGLJson, *>
        val isActive = result.value.getBoolean("isActive")
        Assert.assertEquals(true, isActive)
    }

    @Test
    fun testDecodeInt() {
        val file = File("src/test/QGLJsonFileTest.json")
        val result = qglDecodeJsonFile(file) as Outcome.Success<QGLJson, *>
        val age = result.value.getInt("age")
        Assert.assertEquals(56, age)
    }
}