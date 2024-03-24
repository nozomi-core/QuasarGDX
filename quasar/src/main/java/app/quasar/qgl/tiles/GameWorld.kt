package app.quasar.qgl.tiles

import app.quasar.qgl.render.DrawableApi

interface GameWorld {
    fun onCreate()
    fun onSimulate(delta: Float)
    fun onDraw(draw: DrawableApi)
}