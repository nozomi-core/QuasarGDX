package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.TileId
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TilesetBuilder(
    private val tileSize: Int,
    private val texture: Texture
) {
    private val tileMap = HashMap<Int, TextureRegion>()
    private var largestIndex = -1

    private val defaultRegion = TextureRegion(texture, 0, 0, tileSize ,tileSize)

    fun add(ref: TileId, gridX: Int, gridY: Int, width: Int = tileSize, height: Int = tileSize) {
        if(ref.id > largestIndex) {
            largestIndex = ref.id
        }

        val regionX = mapGridToTextureX(gridX)
        val regionY =  mapGridToTextureY(gridY)

        val textureRegion = TextureRegion(texture, regionX, regionY, width, height)
        tileMap[ref.id] = textureRegion
    }

    fun addSpan(ref: TileId, gridX: Int, gridY: Int, spanWidth: Int, spanHeight: Int) {
        return add(ref, gridX, gridY, spanWidth * tileSize, spanHeight * tileSize)
    }

    private fun mapGridToTextureX(gridX: Int): Int {
        return gridX * tileSize
    }

    private fun mapGridToTextureY(gridY: Int): Int {
        return gridY * tileSize
    }

    fun build(): TileTextures {
        val textureArray = Array(largestIndex + 1) { defaultRegion }
        tileMap.forEach { (index, region) ->
            textureArray[index] = region
        }
        return TileTextures(textureArray)
    }
}