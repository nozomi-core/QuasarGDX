package app.quasar.qgl.test.fixtures

import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.tiles.TileId

class TestEmptyDrawableApi: DrawableApi {
    override fun tileGrid(id: TileId, gridX: Int, gridY: Int) {}

    override fun tilePx(id: TileId, x: Float, y: Float) {}
}