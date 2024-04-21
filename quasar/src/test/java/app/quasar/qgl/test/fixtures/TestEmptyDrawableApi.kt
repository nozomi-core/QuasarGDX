package app.quasar.qgl.test.fixtures

import app.quasar.qgl.engine.core.DrawableApi
import app.quasar.qgl.engine.core.TileId

class TestEmptyDrawableApi: DrawableApi {
    override fun tileGrid(id: TileId, gridX: Int, gridY: Int) {}

    override fun tilePx(id: TileId, x: Float, y: Float) {}
}