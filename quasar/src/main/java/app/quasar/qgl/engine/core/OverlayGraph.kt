package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay

class OverlayGraph(nodeGraph: NodeGraph): GraphListener {

    private val overlayNodes = mutableListOf<GameOverlay>()

    init {
        nodeGraph.addListener(this)
    }

    internal fun draw(dimension: EngineDimension, context: DrawContext) {
        overlayNodes.forEach { overlay ->
            val node = overlay as ReadableGameNode
            if(node.selfDimension.id == dimension.id) {
                overlay.onDrawOverlay(context)
            }
        }
    }

    override fun onAdded(node: GameNode<*>) {
        if(node is GameOverlay) {
            overlayNodes.add(node)
        }
    }

    override fun onRemoved(node: GameNode<*>) {
        if(node is GameOverlay) {
            overlayNodes.remove(node)
        }
    }
}