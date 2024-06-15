package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.StaticPosition
import app.quasar.qgl.engine.core.interfaces.WorldPosition

class WorldGraph(nodeGraph: NodeGraph): GraphListener {

    private val staticNodes = mutableListOf<StaticPosition>()
    private val worldNodes = mutableListOf<WorldPosition>()

    init {
        nodeGraph.addListener(this)
    }

    override fun onAdded(node: GameNode<*>) {
        if(node is WorldPosition) {
            worldNodes.add(node)
        }

        if(node is StaticPosition) {
            staticNodes.add(node)
        }
    }

    override fun onRemoved(node: GameNode<*>) {
        if(node is WorldPosition) {
            worldNodes.remove(node)
        }

        if(node is StaticPosition) {
            staticNodes.add(node)
        }
    }
}