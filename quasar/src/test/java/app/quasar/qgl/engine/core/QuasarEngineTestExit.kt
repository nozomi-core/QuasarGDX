package app.quasar.qgl.engine.core

import app.quasar.qgl._fixtures.TestDrawContext
import org.junit.Assert
import org.junit.Test

class QuasarEngineExitTest {

    @Test
    fun testEngineExitOnlyMarkedButNoCallToExitCompleted() {
        var engineData: EngineDeserialized1? = null

        val engine = QuasarEngineActual1(
            drawContext = TestDrawContext.create(),
            onExit = {
                engineData = it
            },
            deserialized = null,
            frameworkScripts = listOf()
        )

        engine.createNode(BasicScript::class, Unit)
        engine.simulate(10f)
        engine.exit()
        Assert.assertEquals(null, engineData)
    }

    @Test
    fun testEngineOnlyAfterLastSimulationCall() {
        var engineData: EngineDeserialized1? = null

        val engine = QuasarEngineActual1(
            drawContext = TestDrawContext.create(),
            onExit = {
                engineData = it
            },
            deserialized = null,
            frameworkScripts = listOf()
        )

        engine.createNode(BasicScript::class, Unit)
        engine.simulate(10f)
        engine.exit()
        engine.simulate(10f)
        Assert.assertNotNull(engineData)
        Assert.assertEquals(engineData?.currentRuntimeId, 2L)
        Assert.assertEquals(engineData?.graph?.size, 1)
    }
}

class BasicScript: GameNode1<BasicData>() {
    override fun onCreate(input: NodeInput1): BasicData {
        return BasicData("Milo Ramen", 26)
    }
}

data class BasicData(val name: String, val age: Int)