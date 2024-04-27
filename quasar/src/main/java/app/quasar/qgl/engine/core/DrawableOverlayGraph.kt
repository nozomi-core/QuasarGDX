package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay
import app.quasar.qgl.engine.core.interfaces.GameOverlayShape

class DrawableOverlayGraph()  {

    private val nodes = mutableListOf<NodeReference<GameOverlay>>()

    fun add(overlay: GameOverlay) {
        val exists = nodes.find { it.get() == overlay } != null

        if(!exists) {
            nodes.add(NodeReference(overlay))
        }
    }

    /** Since nodes can be removed from the game, we need to clean up any null references since NodeReference will return null is the reference is not active */
    private fun clean() {
        nodes.removeIf { it.get() == null }
    }

    fun draw(context: DrawContext) {
        clean()
        nodes.forEach {
            it.get()?.onDrawOverlay(context)
        }
    }

    fun drawShapes(context: ShapeContext) {
        clean()
        nodes.forEach { reference ->
            val overlay = reference.get()
            if(overlay is GameOverlayShape) {
                overlay.onDrawShape(context)
            }
        }
    }
}