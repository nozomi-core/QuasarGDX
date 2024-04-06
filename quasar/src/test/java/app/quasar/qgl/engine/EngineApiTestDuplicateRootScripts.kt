package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.test.fixtures.TestEmptyDrawableApi
import org.junit.Assert
import org.junit.Test

class Duplicate: GameNode<Unit, Unit>() {}

class EngineApiTestDuplicateRootScripts {

    @Test
    fun testDuplicates() {
        val engineApi = QuasarEngineApi(TestEmptyDrawableApi())

        val didFail = try {
            engineApi.createRootScripts(listOf(Duplicate::class, Duplicate::class))
            false
        } catch (e: Exception) {
            true
        }
        Assert.assertTrue(didFail)
    }
}