package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.test.fixtures.TestEmptyDrawableApi
import org.junit.Test


class EngineSerializeTest {

    @Test
    fun testSerializeScript() {
        val engine = QuasarEngineActual(
            drawableApi = TestEmptyDrawableApi(),
            onExit = { engineData ->

            }
        )

        engine.createGameNode(BasicScript::class, Unit)
        engine.simulate(10f)
    }
}

class BasicScript: GameNode<BasicData, Unit>() {
    override fun onCreate(argument: Unit?): BasicData {
        return BasicData("Milo Ramen", 26)
    }
}

data class BasicData(val name: String, val age: Int)