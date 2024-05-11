package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core1.TileId1
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TileTextures(private val array: Array<TextureRegion>) {

    fun get(id: TileId1): TextureRegion = array[id.id]
}