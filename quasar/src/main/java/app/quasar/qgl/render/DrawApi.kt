package app.quasar.qgl.render

import app.quasar.qgl.tiles.TileId
import app.quasar.qgl.tiles.TileSheetLayout
import app.quasar.qgl.tiles.TileTextures
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class DrawApi(
    private val layout: TileSheetLayout,
    private val textures: TileTextures,
    private val spriteBatch: SpriteBatch
) {

    fun tile(id: TileId, gridX: Int, gridY: Int) {
        val region = textures.get(id)
        spriteBatch.draw(region, (gridX * layout.tileSize).toFloat(), (gridY * layout.tileSize).toFloat())
    }
}