package app.quasar.qgl.engine

import app.quasar.qgl.tiles.GameTileset
import app.quasar.qgl.tiles.TileSheetLayout
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport

data class Quasar2DConfig(
    val texture: Texture,
    val spriteBatch: SpriteBatch,
    val tileset: GameTileset,
    val layout: TileSheetLayout
)

interface QuasarCallbacks {
    fun useWorldCamera(): Camera
    fun useOverlayCamera(): Camera
    fun useWorldViewport(): Viewport
    fun useOverlayViewport(): Viewport
}