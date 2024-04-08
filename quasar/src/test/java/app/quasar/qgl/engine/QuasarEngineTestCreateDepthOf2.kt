package app.quasar.qgl.engine

/**
 * Test  create game node with parent and child, so only a depth of 2 objects on tree
 */
class EngineApiTest {

    /*
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
    }*/
}

interface TestOrder {
    val methodOrder: List<String>
}

/*  TODO :: This test needs to be redone according to new style of functional methods with no side effects
class TestGameNodeParentCreate(): GameNode<Unit, Unit>(), TestOrder {
    override val methodOrder = mutableListOf<String>()

    override fun onSimulate(node: NodeApi, deltaTime: Float, data: Unit?) {
        super.onSimulate(node, deltaTime, data)
        methodOrder.add("parent.onSimulate")
    }

    override fun onCreateData(argument: Unit?): Unit? {
        return super.onCreateData(argument)
        methodOrder.add("parent.onCreateData")
        createChild(TestGameNodeChildCreate::class, null)
    }
}

class TestGameNodeChildCreate(): GameNode<Unit, Unit>() {
    override fun onCreateData(argument: Unit?): Unit? {
        return super.onCreateData(argument)
        val parent =  parentNode as TestGameNodeParentCreate
        parent.methodOrder.add("child.onCreate")
    }

    override fun onSimulate(node: NodeApi, deltaTime: Float, data: Unit?) {
        super.onSimulate(node, deltaTime, data)
        val parent =  parentNode as TestGameNodeParentCreate
        parent.methodOrder.add("child.onSimulate")
    }
}*/