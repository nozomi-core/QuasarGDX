package app.quasar.qgl.engine.core.interfaces

import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.ShapeContext

interface GameOverlay {
    fun onDrawOverlay(context: DrawContext)
}

interface GameOverlayShape {
    fun onDrawShape(context: ShapeContext)
}