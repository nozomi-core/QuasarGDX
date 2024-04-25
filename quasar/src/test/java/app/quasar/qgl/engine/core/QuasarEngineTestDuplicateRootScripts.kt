package app.quasar.qgl.engine.core

import app.quasar.qgl._fixtures.TestDrawContext
import app.quasar.qgl._fixtures.TestEmptyDrawableApi
import org.junit.Assert
import org.junit.Test

class Duplicate: GameNode<Unit, Unit>() {
    override fun onCreate(argument: Unit?) {}
}

class EngineApiTestDuplicateRootScripts {

    @Test
    fun testDuplicates() {
        val engineApi = QuasarEngineActual(TestDrawContext.create(), onExit = {}, data = null, rootScripts = listOf())

        val didFail = try {
            engineApi.createRootScripts(listOf(Duplicate::class, Duplicate::class))
            false
        } catch (e: Exception) {
            true
        }
        Assert.assertTrue(didFail)
    }
}