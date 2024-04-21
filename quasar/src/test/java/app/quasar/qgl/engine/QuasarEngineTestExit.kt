package app.quasar.qgl.engine

import app.quasar.qgl.engine.core.QuasarEngineActual
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.EngineDeserialized
import app.quasar.qgl.test.fixtures.TestEmptyDrawableApi
import org.junit.Assert
import org.junit.Test


class QuasarEngineExitTest {

    @Test
    fun testEngineExitOnlyMarkedButNoCallToExitCompleted() {
        var engineData: EngineDeserialized? = null

        val engine = QuasarEngineActual(
            drawableApi = TestEmptyDrawableApi(),
            onExit = {
                engineData = it
            },
            data = null
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
            drawableApi = TestEmptyDrawableApi(),
            onExit = {
                engineData = it
            },
            data = null
        )

        engine.createGameNode(BasicScript::class, Unit)
        engine.simulate(10f)
        engine.exit()
        engine.simulate(10f)
        Assert.assertNotNull(engineData)
        Assert.assertEquals(engineData?.currentRuntimeId, 2L)
        Assert.assertEquals(engineData?.graph?.nodes?.size, 1)
    }
}

class BasicScript: GameNode<BasicData, Unit>() {
    override fun onCreate(argument: Unit?): BasicData {
        return BasicData("Milo Ramen", 26)
    }
}

data class BasicData(val name: String, val age: Int)