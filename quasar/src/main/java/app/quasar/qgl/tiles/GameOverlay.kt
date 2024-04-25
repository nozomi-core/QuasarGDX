package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.DrawableApi

interface GameOverlay {
    fun onCreate()
    fun onDraw(draw: DrawableApi)
}