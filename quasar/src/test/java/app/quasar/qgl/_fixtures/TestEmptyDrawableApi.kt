package app.quasar.qgl._fixtures

import app.quasar.qgl.engine.core1.*
import app.quasar.qgl.render.SpriteApi
import com.badlogic.gdx.math.Vector3

class TestEmptyDrawableApi: DrawableApi1 {
    override fun tileGrid(id: TileId1, gridX: Int, gridY: Int) {}
    override fun tilePx(id: TileId1, x: Float, y: Float) {}
    override fun tilePx(id: TileId1, vector: Vector3) {}

    override fun tilePx(id: TileId1, x: Float, y: Float, scale: Float, rotation: Float) {}
    override fun batchWith(callback: (SpriteApi) -> Unit) {}
}

class TestEmptyCameraApi: CameraApi1 {
    override fun setCamera(x: Float, y: Float) {}
}

object TestDrawContext {
    fun create() = DrawContext1(TestEmptyDrawableApi(), TestEmptyCameraApi(), OverlayScreen1(90f, 90f)) {}
}