package app.quasar.qgl.engine.core1

import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.engine.core.QuasarEngine
import app.quasar.qgl.engine.core.QuasarEngineActual
import org.junit.Assert
import org.junit.Test

class EngineDataTest {

    private fun createEngine(): QuasarEngine = QuasarEngineActual()

    @Test
    fun testCreateAndFindNode() {

    }

}

class DataScript: GameNode<DataData>() {
    var didCallSimulate = false

    override fun onSimulate() {
        didCallSimulate = true
    }

    override fun onCreate(argument: NodeArgument): DataData {
        return when(argument) {
            is DataArgument -> DataData(argument.number)
            else -> throw nodeException()
        }
    }
}

class DataArgument(val number: Int): NodeArgument
class DataData(number: Int)