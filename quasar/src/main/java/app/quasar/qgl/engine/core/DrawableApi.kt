package app.quasar.qgl.engine.core

import com.badlogic.gdx.math.Vector3

interface DrawableApi {
    fun tilePx(id: TileId, x: Float, y: Float)
    fun tilePx(id: TileId, vector: Vector3)
    fun tilePx(id: TileId, x: Float, y: Float, scale: Float, rotation: Float)
}