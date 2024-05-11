package app.quasar.qgl.engine.core

import app.quasar.qgl._fixtures.TestDrawContext
import org.junit.Assert
import org.junit.Test

class Duplicate: GameNode1<Unit>() {
    override fun onCreate(input: NodeInput1) {}
}

class EngineApiTestDuplicateRootScripts {

    @Test
    fun testDuplicates() {
        val engineApi = QuasarEngineActual1(drawContext = TestDrawContext.create(), onExit = {}, deserialized = null, frameworkScripts = listOf())

        val didFail = try {
            engineApi.createStartScripts(listOf(Duplicate::class, Duplicate::class))
            false
        } catch (e: Exception) {
            true
        }
        Assert.assertTrue(didFail)
    }
}