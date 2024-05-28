package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.OverlayScreen
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.utils.viewport.Viewport

interface GameWindow {
    fun getWorldCamera(): Camera
    fun getOverlayCamera(): Camera
    fun getWorldViewport(): Viewport
    fun getOverlayViewport(): Viewport
    fun getWindow(): OverlayScreen
}