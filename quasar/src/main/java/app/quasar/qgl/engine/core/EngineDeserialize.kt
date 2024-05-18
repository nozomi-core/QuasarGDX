package app.quasar.qgl.engine.core

import app.quasar.qgl.serialize.QGLBinary
import java.io.File
import kotlin.reflect.full.createInstance

class EngineDeserialize(
    filename: String,
    classMap: KlassMap
) {

    lateinit var accounting: EngineAccounting
    lateinit var nodeGraph: NodeGraph

    init {
        val input = QGLBinary.createFileIn(File(filename))
        accounting = readAccounting(input)
        nodeGraph = readNodeGraph(input, classMap)
    }

    private fun readAccounting(binIn: QGLBinary.In): EngineAccounting {
        return EngineAccounting(runtimeGameId = 40000)
    }

    private fun readNodeGraph(binIn: QGLBinary.In, classMap: KlassMap): NodeGraph {
        val nodeList = mutableListOf<GameNode<*>>()

        val playerKClass = classMap.get(3)

        val player = playerKClass.createInstance() as GameNode<*>
        nodeList.add(player)

        player.create(listOf { factory ->
            factory.argument = AnyNodeArgument(Unit)
            factory.nodeId = 4
        })


        return NodeGraph(nodeList)
    }
}