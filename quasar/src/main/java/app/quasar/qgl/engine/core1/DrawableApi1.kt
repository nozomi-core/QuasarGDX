package app.quasar.qgl.engine.core1

import app.quasar.qgl.render.SpriteApi
import com.badlogic.gdx.math.Vector3

interface DrawableApi1 {
    fun tileGrid(id: TileId1, gridX: Int, gridY: Int)
    fun tilePx(id: TileId1, x: Float, y: Float)
    fun tilePx(id: TileId1, vector: Vector3)
    fun tilePx(id: TileId1, x: Float, y: Float, scale: Float, rotation: Float)

    fun batchWith(callback: (SpriteApi) -> Unit)
}