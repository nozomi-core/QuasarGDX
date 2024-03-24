package app.quasar.qgl.tiles

import app.quasar.qgl.render.DrawableApi

interface GameOverlay {
    fun onCreate()
    fun onDraw(drawApi: DrawableApi)
}