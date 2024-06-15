package app.quasar.qgl._fixtures

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.render.SpriteApi
import com.badlogic.gdx.math.Vector3

class TestEmptyDrawableApi: DrawableApi {
    override fun tileGrid(id: TileId, gridX: Int, gridY: Int) {}
    override fun tilePx(id: TileId, x: Float, y: Float) {}
    override fun tilePx(id: TileId, vector: Vector3) {}

    override fun tilePx(id: TileId, x: Float, y: Float, scale: Float, rotation: Float) {}
    override fun batchWith(callback: (SpriteApi) -> Unit) {}
}

class TestEmptyCameraApi: CameraApi {
    override fun setCamera(x: Float, y: Float) {}
}

object TestDrawContext {
    fun create() = DrawContext(TestEmptyDrawableApi(), TestEmptyCameraApi(), OverlayScreen(100f, 100f) ) {}
}