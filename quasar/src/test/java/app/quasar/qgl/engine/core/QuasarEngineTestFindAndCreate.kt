package app.quasar.qgl.engine.core

import app.quasar.qgl._fixtures.TestDrawContext
import org.junit.Assert
import org.junit.Test

/** Tests find based operations in the Engine node graph */
class QuasarEngineTestFindAndCreate {

    private fun createEngine(): QuasarEngine = QuasarEngineActual(
        drawContext = TestDrawContext.create(),
        onExit = {},
        deserialized = null,
        frameworkScripts = listOf()
    )

    @Test
    fun testFindByConcreteClassOnlyInterfaceFindAllowed() {
        val engine = createEngine()

        Assert.assertTrue(try {
            engine.requireFindByInterface(FindScript::class)
            false
        } catch (e: Exception) { true })
    }

    @Test
    fun testFindEmptyNodeGraphThrows() {
        val engine = createEngine()

        Assert.assertTrue(try {
            engine.requireFindByInterface(Find::class)
            false
        } catch (e: Exception) { true })
    }

    @Test
    fun testFindByInterfaceScriptCreatedButPendingEntryIntoGraph() {
        val engine = createEngine()
        engine.createNode(FindScript::class)

        Assert.assertTrue(try {
            engine.requireFindByInterface(Find::class)
            false
        } catch (e: Exception) { true })
    }

    @Test
    fun testFindByInterfaceScriptWasCreated() {
        val engine = createEngine()
        engine.createNode(FindScript::class)
        engine.simulate(10f)

        Assert.assertTrue(try {
            engine.requireFindByInterface(Find::class)
            true
        } catch (e: Exception) { false })
    }
}

interface Find

class FindScript: GameNode<FindData>(), Find {
    override fun onCreate(input: NodeInput): FindData {
        return FindData("popcorn")
    }

}
data class FindData(val title: String)