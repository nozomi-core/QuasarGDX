package app.quasar.qgl.engine.core1

import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.engine.core.QuasarEngine
import app.quasar.qgl.engine.core.QuasarEngineActual
import org.junit.Assert
import org.junit.Test

class EngineCreateTest {

    private fun createEngine(): QuasarEngine = QuasarEngineActual()

    @Test
    fun testCreateAndFindNode() {
        val engine = createEngine()
        val tag = "example-script"

        engine.createNode(ExampleScript::class) { factory ->
            factory.tag = tag
        }
        val exampleNode = engine.findByTag(tag)
        Assert.assertNotNull(exampleNode)
    }

    @Test
    fun testCreateAndSimulateNode() {
        val engine = createEngine()
        val tag = "example-simulate"

        engine.createNode(ExampleScript::class) { factory ->
            factory.tag = tag
        }
        engine.simulate()

        val exampleNode = engine.findByTag(tag) as ExampleScript
        Assert.assertNotNull(exampleNode)
        Assert.assertTrue(exampleNode.didCallSimulate)
    }
}

class ExampleScript: GameNode<ExampleData>() {
    var didCallSimulate = false

    override fun onSimulate() {
        didCallSimulate = true
    }

    override fun onCreate(argument: NodeArgument): ExampleData {
        return ExampleData()
    }
}

class ExampleData