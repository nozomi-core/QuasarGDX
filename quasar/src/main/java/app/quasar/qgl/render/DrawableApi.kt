package app.quasar.qgl.render

import app.quasar.qgl.tiles.TileId

interface DrawableApi {
    fun tileGrid(id: TileId, gridX: Int, gridY: Int)
    fun tilePx(id: TileId, x: Float, y: Float)
}