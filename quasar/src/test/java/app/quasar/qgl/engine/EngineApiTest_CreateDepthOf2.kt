package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.test.fixtures.TestEmptyDrawableApi
import org.junit.Assert
import org.junit.Test

/**
 * Test  create game node with parent and child, so only a depth of 2 objects on tree
 */
class EngineApiTest {

    @Test
    fun testGameNodeCreateRootSequence() {
        val testEngine = QuasarEngineApi(TestEmptyDrawableApi())
        testEngine.createGameNode(TestGameNodeParentCreate::class)
        testEngine.simulate(1f)

        val parentObject = testEngine.requireFindByInterface(TestOrder::class)
        Assert.assertArrayEquals(
            arrayOf("parent.onCreate", "parent.onSimulate", "child.onCreate"),
            parentObject.methodOrder.toTypedArray())
    }

    @Test
    fun testGameNodeSimulation() {
        val testEngine = QuasarEngineApi(TestEmptyDrawableApi())
        testEngine.createGameNode(TestGameNodeParentCreate::class)
        testEngine.simulate(1f)

        testEngine.simulate(1f)

        val parentObject = testEngine.requireFindByInterface(TestOrder::class)
        Assert.assertArrayEquals(
            //This implies that when a parent calls `createChild` it will be created in the current step, but wont get simulated until next frame
            arrayOf("parent.onCreate","parent.onSimulate", "child.onCreate", "parent.onSimulate", "child.onSimulate"),
            parentObject.methodOrder.toTypedArray())
    }
}

interface TestOrder {
    val methodOrder: List<String>
}

class TestGameNodeParentCreate(): GameNode(), TestOrder {
    override val methodOrder = mutableListOf<String>()

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        methodOrder.add("parent.onSimulate")
    }

    override fun onCreate(engineApi: EngineApi, argument: Any?) {
        super.onCreate(engineApi, argument)
        methodOrder.add("parent.onCreate")
        createChild(TestGameNodeChildCreate::class, null)
    }
}

class TestGameNodeChildCreate(): GameNode() {
    override fun onCreate(engineApi: EngineApi, argument: Any?) {
        super.onCreate(engineApi, argument)
        val parent =  parentNode as TestGameNodeParentCreate
        parent.methodOrder.add("child.onCreate")
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        val parent =  parentNode as TestGameNodeParentCreate
        parent.methodOrder.add("child.onSimulate")
    }
}