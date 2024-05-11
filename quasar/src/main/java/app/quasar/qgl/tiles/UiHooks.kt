package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core1.OverlayScreen1
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport

data class QuasarEngine2DConfig(
    val texture: Texture,
    val spriteBatch: SpriteBatch,
    val tileset: GameTileset,
    val layout: TileSheetLayout,
    val screen: OverlayScreen1
)

interface UiHooks {
    fun useWorldCamera(): Camera
    fun useOverlayCamera(): Camera
    fun useWorldViewport(): Viewport
    fun useOverlayViewport(): Viewport
}