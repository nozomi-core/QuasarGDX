package app.quasar.qgl.engine.core.interfaces

import app.quasar.qgl.engine.core.DrawContext
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

interface GameOverlay {
    fun onDrawOverlay(context: DrawContext)
}

interface GameOverlayShape {
    fun onDrawShape(shape: ShapeRenderer)
}