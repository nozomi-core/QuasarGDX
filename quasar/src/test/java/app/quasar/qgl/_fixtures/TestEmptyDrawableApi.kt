package app.quasar.qgl._fixtures

import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.DrawableApi
import app.quasar.qgl.engine.core.TileId
import app.quasar.qgl.render.SpriteApi

class TestEmptyDrawableApi: DrawableApi {
    override fun tileGrid(id: TileId, gridX: Int, gridY: Int) {}
    override fun tilePx(id: TileId, x: Float, y: Float) {}
    override fun tilePx(id: TileId, x: Float, y: Float, scale: Float, rotation: Float) {}
    override fun batchWith(callback: (SpriteApi) -> Unit) {}
}

object TestDrawContext {
    fun create() = DrawContext(TestEmptyDrawableApi())
}