package app.quasar.qgl.engine.core

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3

interface DrawableApi {
    val defaultFont: BitmapFont

    fun tilePx(id: SpriteId, x: Float, y: Float)
    fun tilePx(id: SpriteId, vector: Vector3)
    fun tilePx(id: SpriteId,vector: Vector3, scale: Float, rotation: Float)
    fun tilePx(id: SpriteId, x: Float, y: Float, scale: Float, rotation: Float)

    fun text(layout: GlyphLayout, x: Float, y: Float)
    fun shape(callback: (ShapeRenderer) -> Unit)
    fun texture(texture: Texture, x: Float, y: Float)
}