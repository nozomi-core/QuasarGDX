package app.quasar.qgl.render

import app.quasar.qgl.engine.core1.DrawableApi1
import app.quasar.qgl.engine.core.TileId
import app.quasar.qgl.tiles.TileTextures
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3

class DrawableApiActual1(
    private val tileSize: Int,
    private val textures: TileTextures,
    private val spriteBatch: SpriteBatch
): DrawableApi1 {

    private val spriteApi = object : SpriteApi {
        override fun setColor(color: Color) {
            spriteBatch.color = color
        }

        override fun setAlpha(alpha: Float) {
            val color = spriteBatch.color.cpy()
            color.a = alpha
            spriteBatch.color = color
        }
    }
    override fun tileGrid(id: TileId, gridX: Int, gridY: Int) {
        val region = textures.get(id)
        spriteBatch.draw(region, (gridX * tileSize).toFloat(), (gridY * tileSize).toFloat())
    }

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

    override fun batchWith(callback: (SpriteApi) -> Unit) {
        val oldColor = spriteBatch.color.cpy()
        callback(spriteApi)

        //Revert state changes made in callback
        spriteBatch.color = oldColor
    }
}

interface SpriteApi {
    fun setColor(color: Color)
    fun setAlpha(alpha: Float)
}