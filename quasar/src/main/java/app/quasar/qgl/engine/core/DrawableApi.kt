package app.quasar.qgl.engine.core

interface DrawableApi {
    fun tileGrid(id: TileId, gridX: Int, gridY: Int)
    fun tilePx(id: TileId, x: Float, y: Float)
}