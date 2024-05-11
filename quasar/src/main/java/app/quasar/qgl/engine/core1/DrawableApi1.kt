package app.quasar.qgl.engine.core1

import app.quasar.qgl.engine.core.TileId
import com.badlogic.gdx.math.Vector3

interface DrawableApi1 {
    fun tileGrid(id: TileId, gridX: Int, gridY: Int)
    fun tilePx(id: TileId, x: Float, y: Float)
    fun tilePx(id: TileId, vector: Vector3)
    fun tilePx(id: TileId, x: Float, y: Float, scale: Float, rotation: Float)
}