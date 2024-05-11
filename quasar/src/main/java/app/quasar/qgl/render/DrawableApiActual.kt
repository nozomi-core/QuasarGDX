package app.quasar.qgl.render

import app.quasar.qgl.engine.core.DrawableApi
import app.quasar.qgl.engine.core.TileId
import app.quasar.qgl.tiles.TileTextures
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3

class DrawableApiActual(
    private val textures: TileTextures,
    private val spriteBatch: SpriteBatch
): DrawableApi {

    override fun tilePx(id: TileId, x: Float, y: Float) {
        val region = textures.get(id)
        spriteBatch.draw(region, x, y)
    }

    override fun tilePx(id: TileId, vector: Vector3) {
        tilePx(id, vector.x, vector.y)
    }

    override fun tilePx(id: TileId, x: Float, y: Float, scale: Float, rotation: Float) {
        val region = textures.get(id)
        spriteBatch.draw(region, x, y, region.regionWidth / 2f, region.regionHeight / 2f,  region.regionWidth.toFloat(), region.regionHeight.toFloat(), scale, scale, rotation)
    }
}