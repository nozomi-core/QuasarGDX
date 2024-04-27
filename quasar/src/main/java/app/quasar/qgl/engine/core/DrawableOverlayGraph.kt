package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay
import app.quasar.qgl.engine.core.interfaces.GameOverlayShape

class DrawableOverlayGraph(nodeGraph: NodeGraph): GraphChangedListener {

    private val overlayCalls = mutableListOf<GameOverlay>()

    init {
        nodeGraph.addListener(this)
    }

    fun draw(context: DrawContext) {
        overlayCalls.forEach { overlay ->
            overlay.onDrawOverlay(context)

            if(overlay is GameNode<*>) {
                overlay.getChildOverlays().draw(context)
                drawChildOverlay(context, overlay.getChildNodes())
            }
        }
    }

    fun drawShapes(context: ShapeContext) {
        overlayCalls.forEach { overlay ->
            if(overlay is GameOverlayShape) {
                overlay.onDrawShape(context)
            }

            if(overlay is GameNode<*>) {
                overlay.getChildOverlays().drawShapes(context)
                drawChildShapes(context, overlay.getChildNodes())
            }
        }
    }

    private fun drawChildShapes(context: ShapeContext, graph: NodeGraph) {
        graph.forEach { child ->
            child.getChildOverlays().drawShapes(context)
            drawChildShapes(context, child.getChildNodes())
        }
    }

    private fun drawChildOverlay(context: DrawContext, graph: NodeGraph) {
        graph.forEach { child ->
            child.getChildOverlays().draw(context)
            drawChildOverlay(context, child.getChildNodes())
        }
    }

    override fun onAdded(node: GameNode<*>) {
        if(node is GameOverlay) {
            overlayCalls.add(node)
        }
    }

    override fun onRemoved(node: GameNode<*>) {
        if(node is GameOverlay) {
            overlayCalls.remove(node)
        }
    }
}