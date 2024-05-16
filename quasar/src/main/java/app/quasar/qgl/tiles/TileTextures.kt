package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.SpriteId
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TileTextures(private val tileMap: Map<String,TextureRegion>) {
    fun get(id: SpriteId): TextureRegion = tileMap[id.id]!!
}