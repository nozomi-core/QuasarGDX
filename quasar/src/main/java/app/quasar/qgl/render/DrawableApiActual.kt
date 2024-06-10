package app.quasar.qgl.render

import app.quasar.qgl.engine.core.DrawableApi
import app.quasar.qgl.engine.core.SpriteId
import app.quasar.qgl.tiles.TileTextures
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3

class DrawableApiActual(
    private val textures: TileTextures,
    private val spriteBatch: SpriteBatch,
    private val font: BitmapFont,
    private val shapeRenderer: ShapeRenderer
): DrawableApi {

    override val defaultFont get() = font

    override fun tilePx(id: SpriteId, x: Float, y: Float) {
        val region = textures.get(id)
        spriteBatch.draw(region, x, y)
    }

    override fun tilePx(id: SpriteId, vector: Vector3) {
        tilePx(id, vector.x, vector.y)
    }

    override fun tilePx(id: SpriteId, vector: Vector3, scale: Float, rotation: Float) {
        tilePx(id, vector.x, vector.y, scale, rotation)
    }

    override fun tilePx(id: SpriteId, x: Float, y: Float, scale: Float, rotation: Float) {
        val region = textures.get(id)
        spriteBatch.draw(region, x, y, region.regionWidth / 2f, region.regionHeight / 2f,  region.regionWidth.toFloat(), region.regionHeight.toFloat(), scale, scale, rotation)
    }

    override fun text(layout: GlyphLayout, x: Float, y: Float) {
        spriteBatch.end()

        spriteBatch.begin()
        font.draw(spriteBatch,layout, x, y)

        spriteBatch.end()
        spriteBatch.begin()
    }

    override fun shape(callback: (ShapeRenderer) -> Unit) {
        spriteBatch.end()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        callback(shapeRenderer)
        shapeRenderer.end()
        spriteBatch.begin()
    }

    override fun texture(texture: Texture, x: Float, y: Float) {
        spriteBatch.draw(texture, x,  y)
    }

    override fun setAlpha(alpha: Float) {
        spriteBatch.setColor(spriteBatch.color.r, spriteBatch.color.g, spriteBatch.color.b, alpha)
    }
}