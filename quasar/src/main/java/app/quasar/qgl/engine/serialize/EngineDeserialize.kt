package app.quasar.qgl.engine.serialize

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.serialize.*
import java.io.File
import kotlin.reflect.KClass

class EngineDeserialize(
    classFactory: ClassFactory,
    factory: () -> QGLBinary.In
) {
    internal var accounting: EngineAccounting
    internal var nodeGraph: NodeGraph
    internal var dimension: EngineDimension

    private val kClassMap: KClassMap

    private val dataIn: QGLBinary.In
    private val coffeeBin: CoffeeBin.In

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


        dataIn = factory()
        coffeeBin = CoffeeBin().In(kClassMap, dataIn)
        accounting = readAccounting()
        nodeGraph = readNodeGraph()
        dimension = readDimension()
    }

    private fun readAccounting(): EngineAccounting {
        val output = BinaryOutput()
        dataIn.read(output)
        return EngineAccounting(runtimeGameId = output.data as Long)
    }

    private fun readDimension(): EngineDimension {
        val output = BinaryOutput()
        dataIn.read(output)
        return EngineDimension.create(id = output.data as Int)
    }

    private fun readNodeGraph(): NodeGraph {
        val output = BinaryOutput()
        dataIn.read(output)

        val nodeSize = output.data as Int
        val nodeList = mutableListOf<GameNode<*>>()

        repeat(nodeSize) {
            val node = readGameNode(output)
            nodeList.add(node)
        }

        return NodeGraph(nodeList)
    }

    private fun readNodeId(output: BinaryOutput): Long {
        dataIn.read(output)
        return output.data as Long
    }

    private fun readTag(output: BinaryOutput): String {
        dataIn.read(output)
        return output.data as String
    }

    private fun readDimension(output: BinaryOutput): EngineDimension {
        dataIn.read(output)
        return EngineDimension.create(output.data as Int)
    }

    private fun readGameNode(
        output: BinaryOutput
    ): GameNode<*> {
        val nodeId = readNodeId(output)
        val tag = readTag(output)
        val dimension = readDimension(output)

        val nodeScript = coffeeBin.readObjectRecord() as GameNode<Any>
        val nodeData = coffeeBin.readObjectRecord()

        nodeScript.record = NodeRecord(
            data = nodeData,
            nodeId = nodeId,
            tag = tag,
            dimension = dimension
        )
        return nodeScript
    }
}