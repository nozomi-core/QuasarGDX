package app.quasar.qgl.engine.core1.interfaces

import app.quasar.qgl.engine.core1.DrawContext1
import app.quasar.qgl.engine.core1.ShapeContext1

interface GameOverlay1 {
    fun onDrawOverlay(context: DrawContext1)
}

interface GameOverlayShape1 {
    fun onDrawShape(context: ShapeContext1)
}