package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.SpriteId
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TilesetBuilder(
    private val tileSize: Int,
    private val texture: Texture
) {
    private val tileMap = HashMap<String, TextureRegion>()

    fun add(ref: SpriteId, gridX: Int, gridY: Int, width: Int = tileSize, height: Int = tileSize) {
        val regionX = mapGridToTextureX(gridX)
        val regionY =  mapGridToTextureY(gridY)

        val textureRegion = TextureRegion(texture, regionX, regionY, width, height)
        tileMap[ref.id] = textureRegion
    }

    fun addSpan(ref: SpriteId, gridX: Int, gridY: Int, spanWidth: Int, spanHeight: Int) {
        return add(ref, gridX, gridY, spanWidth * tileSize, spanHeight * tileSize)
    }

    private fun mapGridToTextureX(gridX: Int): Int {
        return gridX * tileSize
    }

    private fun mapGridToTextureY(gridY: Int): Int {
        return gridY * tileSize
    }

    fun build(): TileTextures {
        return TileTextures(tileMap)
    }
}