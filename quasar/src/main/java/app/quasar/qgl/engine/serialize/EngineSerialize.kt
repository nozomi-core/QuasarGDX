package app.quasar.qgl.engine.serialize

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.serialize.*

class EngineSerialize(
    engine: QuasarEngineActual,
    factory: () -> QGLBinary.Out
) {
    private val dataOut: QGLBinary.Out
    private val coffeeBin: CoffeeBin.Out

    init {
        dataOut = factory()
        coffeeBin = CoffeeBin().Out(dataOut)
        writeAccounting(engine.accounting)
        writeNodeGraph(engine.nodeGraph)
        dataOut.close()
    }

    private fun writeAccounting(accounting: EngineAccounting) {
        dataOut.writeLong(0, accounting.runtimeGameId)
    }

    private fun writeNodeGraph(graph: NodeGraph) {
        dataOut.writeInt(0, graph.size)

        graph.forEach { node ->
            dataOut.writeLong(0, node.record.nodeId)
            dataOut.writeString(0, node.record.tag)

            coffeeBin.writeObjectRecord(node)
            coffeeBin.writeObjectRecord(node.record.data!!)
        }
    }
}