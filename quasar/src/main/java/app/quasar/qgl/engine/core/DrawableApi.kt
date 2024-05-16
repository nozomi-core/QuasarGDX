package app.quasar.qgl.engine.core

import com.badlogic.gdx.math.Vector3

interface DrawableApi {
    fun tilePx(id: SpriteId, x: Float, y: Float)
    fun tilePx(id: SpriteId, vector: Vector3)
    fun tilePx(id: SpriteId, x: Float, y: Float, scale: Float, rotation: Float)
}