package app.quasar.qgl.fixtures

import app.quasar.qgl.engine.core.DrawableApi
import app.quasar.qgl.engine.core.SpriteId
import com.badlogic.gdx.math.Vector3

class EmptyDrawableApi: DrawableApi {
    override fun tilePx(id: SpriteId, x: Float, y: Float) {

    }

    override fun tilePx(id: SpriteId, vector: Vector3) {

    }

    override fun tilePx(id: SpriteId, vector: Vector3, scale: Float, rotation: Float) {

    }

    override fun tilePx(id: SpriteId, x: Float, y: Float, scale: Float, rotation: Float) {

    }
}