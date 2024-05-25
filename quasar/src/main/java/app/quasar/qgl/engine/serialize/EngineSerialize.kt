package app.quasar.qgl.engine.serialize

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.serialize.*
import java.io.File
import kotlin.reflect.KClass

class EngineSerialize(
    engine: QuasarEngineActual,
    filename: String,
    classFactory: ClassFactory
) {

    private val kClassMap: KClassMap


    init {
        val nodeScripts = ScriptBuilder().apply {
            applyScripts(classFactory.scriptFactory)
        }

        val dataScripts = DataBuilder().apply {
            applyScripts(classFactory.dataFactory)
        }

        kClassMap = KClassMap(mutableListOf<KClass<*>>().apply {
            addAll(nodeScripts.getList())
            addAll(dataScripts.getList())
        })

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
            writeScriptClass(node)
            writeScriptData(node.record.data)
        }
    }

    private fun writeScriptClass(node: GameNode<*>) {

    }

    private fun writeScriptData(gameData: GameData) {

    }
}