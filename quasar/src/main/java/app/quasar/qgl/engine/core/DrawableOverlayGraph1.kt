package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay1
import app.quasar.qgl.engine.core.interfaces.GameOverlayShape1

class DrawableOverlayGraph1()  {

    private val nodes = mutableListOf<NodeReference1<GameOverlay1>>()

    fun add(overlay: GameOverlay1) {
        val exists = nodes.find { it.get() == overlay } != null

        if(!exists) {
            nodes.add(NodeReference1(overlay))
        }
    }

    /** Since nodes can be removed from the game, we need to clean up any null references since NodeReference will return null is the reference is not active */
    private fun clean() {
        nodes.removeIf { it.get() == null }
    }

    fun draw(context: DrawContext1) {
        clean()
        nodes.forEach {
            it.get()?.onDrawOverlay(context)
        }
    }

    fun drawShapes(context: ShapeContext1) {
        clean()
        nodes.forEach { reference ->
            val overlay = reference.get()
            if(overlay is GameOverlayShape1) {
                overlay.onDrawShape(context)
            }
        }
    }
}