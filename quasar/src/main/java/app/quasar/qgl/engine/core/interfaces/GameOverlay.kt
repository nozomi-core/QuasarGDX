package app.quasar.qgl.engine.core.interfaces

import app.quasar.qgl.engine.core.DrawContext

interface GameOverlay {
    fun onDrawOverlay(context: DrawContext)
}