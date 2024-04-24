package app.quasar.qgl.engine.core

import app.quasar.qgl.render.SpriteApi

interface DrawableApi {
    fun tileGrid(id: TileId, gridX: Int, gridY: Int)
    fun tilePx(id: TileId, x: Float, y: Float)
    fun tilePx(id: TileId, x: Float, y: Float, scale: Float, rotation: Float)

    fun batchWith(callback: (SpriteApi) -> Unit)
}