package app.quasar.qgl.engine

import app.quasar.qgl.tiles.GameTileset
import app.quasar.qgl.tiles.TileSheetLayout
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface QuasarCallbacks {
    fun getTexture(): Texture
    fun getSpriteBatch(): SpriteBatch
    fun getGameTileset(): GameTileset
    fun getCamera(): Camera
    fun getTileLayout(): TileSheetLayout
}