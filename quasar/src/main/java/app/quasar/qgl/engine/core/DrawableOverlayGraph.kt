package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay

class DrawableOverlayGraph(nodeGraph: NodeGraph): GraphChangedListener {

    private val overlayCalls = mutableListOf<GameOverlay>()

    init {
        nodeGraph.addListener(this)
    }

    fun draw(context: DrawContext) {
        overlayCalls.forEach { it.onDrawOverlay(context) }
    }

    override fun onAdded(node: GameNode<*, *>) {
        if(node is GameOverlay) {
            overlayCalls.add(node)
        }
    }

    override fun onRemoved(node: GameNode<*, *>) {
        if(node is GameOverlay) {
            overlayCalls.remove(node)
        }
    }
}