package app.quasar.qgl.engine.core.interfaces

import app.quasar.qgl.engine.core.DrawContext1
import app.quasar.qgl.engine.core.ShapeContext1

interface GameOverlay1 {
    fun onDrawOverlay(context: DrawContext1)
}

interface GameOverlayShape1 {
    fun onDrawShape(context: ShapeContext1)
}