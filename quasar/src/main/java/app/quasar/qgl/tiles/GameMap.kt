package app.quasar.qgl.tiles

import app.quasar.qgl.render.DrawApi

interface GameMap {
    fun onCreate()
    fun onDraw(draw: DrawApi)
}