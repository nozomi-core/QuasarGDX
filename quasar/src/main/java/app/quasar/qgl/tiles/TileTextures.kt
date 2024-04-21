package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.TileId
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TileTextures(private val array: Array<TextureRegion>) {

    fun get(id: TileId): TextureRegion = array[id.tileId]
}