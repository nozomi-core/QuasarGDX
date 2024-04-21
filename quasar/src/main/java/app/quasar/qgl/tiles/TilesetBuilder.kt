package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.TileId
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TilesetBuilder(
    private val layout: TileSheetLayout,
    private val texture: Texture
) {
    private val tileMap = HashMap<Int, TextureRegion>()
    private var largestIndex = -1

    private val defaultRegion = TextureRegion(texture, 0, 0, layout.tileSize ,layout.tileSize)

    fun add(ref: TileId, gridX: Int, gridY: Int) {
        if(ref.id > largestIndex) {
            largestIndex = ref.id
        }

        val regionX = mapGridToTextureX(gridX)
        val regionY =  mapGridToTextureY(gridY)

        val textureRegion = TextureRegion(texture, regionX, regionY, layout.tileSize, layout.tileSize)
        tileMap[ref.id] = textureRegion
    }

    private fun mapGridToTextureX(gridX: Int): Int {
        return gridX * layout.tileSize
    }

    private fun mapGridToTextureY(gridY: Int): Int {
        return gridY * layout.tileSize
    }

    fun build(): TileTextures {
        val textureArray = Array(largestIndex + 1) { defaultRegion }
        tileMap.forEach { (index, region) ->
            textureArray[index] = region
        }
        return TileTextures(textureArray)
    }
}