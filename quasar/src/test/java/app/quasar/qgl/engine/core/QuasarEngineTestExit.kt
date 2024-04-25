package app.quasar.qgl.engine.core

import app.quasar.qgl._fixtures.TestDrawContext
import app.quasar.qgl._fixtures.TestEmptyDrawableApi
import org.junit.Assert
import org.junit.Test

class QuasarEngineExitTest {

    @Test
    fun testEngineExitOnlyMarkedButNoCallToExitCompleted() {
        var engineData: EngineDeserialized? = null

        val engine = QuasarEngineActual(
            TestDrawContext.create(),
            onExit = {
                engineData = it
            },
            data = null,
            rootScripts = listOf(),
            engineHooks = null
        )

        engine.createGameNode(BasicScript::class, Unit)
        engine.simulate(10f)
        engine.exit()
        Assert.assertEquals(null, engineData)
    }

    @Test
    fun testEngineOnlyAfterLastSimulationCall() {
        var engineData: EngineDeserialized? = null

        val engine = QuasarEngineActual(
            TestDrawContext.create(),
            onExit = {
                engineData = it
            },
            data = null,
            rootScripts = listOf(),
            engineHooks = null)

        engine.createGameNode(BasicScript::class, Unit)
        engine.simulate(10f)
        engine.exit()
        engine.simulate(10f)
        Assert.assertNotNull(engineData)
        Assert.assertEquals(engineData?.currentRuntimeId, 2L)
        Assert.assertEquals(engineData?.graph?.size, 1)
    }
}

class BasicScript: GameNode<BasicData, Unit>() {
    override fun onCreate(argument: Unit?): BasicData {
        return BasicData("Milo Ramen", 26)
    }
}

data class BasicData(val name: String, val age: Int)