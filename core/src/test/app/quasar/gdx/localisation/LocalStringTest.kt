package app.quasar.gdx.localisation

import app.quasar.qgl.serialize.qglDecodeJson
import org.junit.Assert
import org.junit.Test
import java.io.File

class LocalStringTest {

    @Test
    fun testEnglishStringFileExists() {
        val file = File( "strings/eng.json")
        Assert.assertEquals(true, file.exists())
    }

    @Test
    fun testLocalString() {
        Assert.assertEquals("hello debug", LocalString.debug.toString())
    }

    @Test
    fun testAllStringIdsExistsOnDisk() {
        LocalLanguage.values().forEach { language ->
            changeRuntimeLanguage(language)

            LocalString.values().forEach {  localString ->
                localString.toString()
            }
        }
    }

    @Test
    fun testThrowException() {
        val json = qglDecodeJson("{\"no_key\":\"debug\"}").getOrNull()!!

        val didThrow = try {
            json.getLanguageResource(LocalString.debug)
            false
        } catch (e: Exception) {
            true
        }
        Assert.assertEquals(true, didThrow)
    }
}