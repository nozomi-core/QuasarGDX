package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay
import app.quasar.qgl.engine.core.interfaces.ShapeOverlay

class OverlayGraph(nodeGraph: NodeGraph): GraphListener {

    private val overlayNodes = mutableListOf<GameOverlay>()
    private val shapeNodes = mutableListOf<ShapeOverlay>()

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

    internal fun drawOverlayShapes(dimension: EngineDimension, context: ShapeApi) {
        shapeNodes.forEach { overlay ->
            val node = overlay as ReadableGameNode
            if(node.selfDimension.id == dimension.id) {
                overlay.onShape(context)
            }
        }
    }

    override fun onAdded(node: GameNode<*>) {
        if(node is GameOverlay) {
            overlayNodes.add(node)
        }

        if(node is ShapeOverlay) {
            shapeNodes.add(node)
        }
    }

    override fun onRemoved(node: GameNode<*>) {
        if(node is GameOverlay) {
            overlayNodes.remove(node)
        }

        if(node is ShapeOverlay) {
            shapeNodes.remove(node)
        }
    }
}