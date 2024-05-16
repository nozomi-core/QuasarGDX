package app.quasar.qgl.tiles

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.utils.viewport.Viewport

interface GameWindow {
    fun getWorldCamera(): Camera
    fun getOverlayCamera(): Camera
    fun getWorldViewport(): Viewport
    fun getOverlayViewport(): Viewport
}