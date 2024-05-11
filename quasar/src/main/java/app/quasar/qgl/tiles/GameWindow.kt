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
    val tileSize: Int,
    val screen: OverlayScreen1
)

interface GameWindow {
    fun getWorldCamera(): Camera
    fun getOverlayCamera(): Camera
    fun getWorldViewport(): Viewport
    fun getOverlayViewport(): Viewport
}