package app.quasar.qgl.engine.core

import app.quasar.qgl.serialize.*
import java.io.File
import kotlin.reflect.full.createInstance

class EngineDeserialize(
    filename: String,
    scriptFactory: ScriptFactory
) {

    internal var accounting: EngineAccounting
    internal var nodeGraph: NodeGraph

    private val scripts = ScriptBuilder().apply {
        applyScripts(scriptFactory)
    }

    init {
        val input = QGLBinary.createFileIn(File(filename))
        accounting = readAccounting(input)
        nodeGraph = readNodeGraph(input)
    }

    private fun readAccounting(binIn: QGLBinary.In): EngineAccounting {
        val output = BinaryOutput()
        binIn.read(output)
        return EngineAccounting(runtimeGameId = output.data as Long)
    }

    private fun readNodeGraph(binIn: QGLBinary.In): NodeGraph {
        val output = BinaryOutput()

        binIn.read(output)

        val nodeSize = output.data as Int
        val nodeList = mutableListOf<GameNode<*>>()

        repeat(nodeSize) {
            val nodeId = readNodeId(binIn, output)
            val tag = readTag(binIn, output)

            readGameNode(binIn, output, nodeList, nodeId, tag)
        }

        return NodeGraph(nodeList)
    }

    private fun readNodeId(binIn: QGLBinary.In, output: BinaryOutput): Long {
        binIn.read(output)
        return output.data as Long
    }

    private fun readTag(binIn: QGLBinary.In, output: BinaryOutput): String {
        binIn.read(output)
        return output.data as String
    }

    private fun readGameNode(
        binIn: QGLBinary.In,
        output: BinaryOutput,
        list: MutableList<GameNode<*>>,
        nodeId: Long,
        tag: String
    ) {
        //Read and create game node
        binIn.read(output)
        val nodeDef = scripts.getDefinitionById(output.id)
        val script = nodeDef?.kClass!!
        val mapper = nodeDef.mapper as QGLMapper<Any>

        val data = mapper.toEntity(output.data as BinaryObject)
        val gameNode = script.createInstance() as GameNode<Any>

        gameNode.record = NodeRecord(
            data = data,
            nodeId = nodeId,
            tag = tag
        )

        list.add(gameNode)
    }
}