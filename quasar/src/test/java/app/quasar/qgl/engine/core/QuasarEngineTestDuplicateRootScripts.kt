package app.quasar.qgl.engine.core

import app.quasar.qgl._fixtures.TestDrawContext
import org.junit.Assert
import org.junit.Test

class Duplicate: GameNode<Unit, Unit>() {
    override fun onCreate(argument: Unit?) {}
}

class EngineApiTestDuplicateRootScripts {

    @Test
    fun testDuplicates() {
        val engineApi = QuasarEngineActual(drawContext = TestDrawContext.create(), onExit = {}, data = null, rootScripts = listOf())

        val didFail = try {
            engineApi.createStartScripts(listOf(Duplicate::class, Duplicate::class))
            false
        } catch (e: Exception) {
            true
        }
        Assert.assertTrue(didFail)
    }
}