package app.quasar.qgl.engine.core

import app.quasar.qgl.serialize.*
import java.io.File
import kotlin.reflect.KClass

class EngineSerialize(
    engine: QuasarEngineActual,
    filename: String,
    scriptFactory: ScriptFactory,
) {
    private val scripts = ScriptBuilder().apply {
        applyScripts(scriptFactory)
    }

    init {
        QGLBinary.createFileOut(File("${filename}.qgl")) { out ->
            writeAccounting(out, engine.accounting)
            writeNodeGraph(out, engine.nodeGraph)
        }
    }

    private fun writeAccounting(out: QGLBinary.Out, accounting: EngineAccounting) {
        out.writeLong(0, accounting.runtimeGameId)
    }

    private fun writeNodeGraph(out: QGLBinary.Out, graph: NodeGraph) {
        out.writeInt(0, graph.size)

        graph.forEach { node ->
            val definition = scripts.getDefinitionByClass(node::class)

            if(definition != null) {
                val mapper = definition.mapper as QGLMapper<Any>
                val binaryRecord = mapper.toBinary(node.record.data!!)

                out.writeLong(0, node.nodeId)
                out.writeString(0, node.tag)
                out.writeObject(definition.id, BinaryObject(definition.id, binaryRecord))

            } else {
                //TODO: log no definition for script or throw exception TBA
            }
        }
    }
}