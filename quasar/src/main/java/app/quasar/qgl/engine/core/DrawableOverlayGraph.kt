package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay
import app.quasar.qgl.engine.core.interfaces.GameOverlayShape
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class DrawableOverlayGraph(nodeGraph: NodeGraph): GraphChangedListener {

    private val overlayCalls = mutableListOf<GameOverlay>()

    init {
        nodeGraph.addListener(this)
    }

    fun draw(context: DrawContext) {
        overlayCalls.forEach { it.onDrawOverlay(context) }
    }

    fun drawShapes(shape: ShapeRenderer) {
        overlayCalls.forEach { overlay ->
            if(overlay is GameOverlayShape) {
                overlay.onDrawShape(shape)
            }
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