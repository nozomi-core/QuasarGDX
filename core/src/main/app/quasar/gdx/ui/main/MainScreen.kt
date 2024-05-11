package app.quasar.gdx.ui.main

import app.quasar.gdx.CoreAssets
import app.quasar.gdx.CoreWorld
import app.quasar.gdx.tiles.CoreTileset
import app.quasar.qgl.QuasarRuntime
import app.quasar.qgl.tiles.GameWindow
import app.quasar.qgl.engine.Quasar2DEngine
import app.quasar.qgl.engine.core1.OverlayScreen1
import app.quasar.qgl.tiles.QuasarEngine2DConfig
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class MainScreen(private val runtime: QuasarRuntime): Screen {

    private lateinit var worldCamera: OrthographicCamera
    private lateinit var overlayCamera: OrthographicCamera

    private lateinit var worldViewport: Viewport
    private lateinit var overlayViewport: Viewport
    private lateinit var engine2D: Quasar2DEngine

    private val screen = OverlayScreen1(400f, 400f)

    private val config by lazy {
        QuasarEngine2DConfig(
            texture = Texture(CoreAssets.Sprites.TILE_SET),
            spriteBatch = SpriteBatch(),
            tileset = CoreTileset(),
            tileSize = 16,
            screen = OverlayScreen1(400f, 400f)
        )
    }

    private val engineCallbacks = object : GameWindow {
        override fun getWorldCamera() = worldCamera
        override fun getWorldViewport() = worldViewport
        override fun getOverlayCamera() = overlayCamera
        override fun getOverlayViewport() = overlayViewport
    }

    override fun show() {
        engine2D = Quasar2DEngine(runtime, config, engineCallbacks).apply {
            createWorld(CoreWorld::class)
        }
        worldCamera = OrthographicCamera()
        overlayCamera = OrthographicCamera()
        worldViewport = ExtendViewport(320f, 180f, worldCamera)
        overlayViewport = FitViewport(screen.width, screen.height, overlayCamera)
        overlayCamera.translate(200f, 200f)
    }

    override fun render(delta: Float) {
        handleInput()
        ScreenUtils.clear(1f, 1f, 1f, 1f)
        engine2D.render()
    }

    private fun handleInput() {
        // Move the camera based on input events
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            worldCamera.translate(-1f, 0f, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            worldCamera.translate(1f, 0f, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            worldCamera.translate(0f, 1f, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            worldCamera.translate(0f, -1f, 0f)
        }
    }

    override fun resize(width: Int, height: Int) {
        worldViewport.update(width, height)
        overlayViewport.update(width, height)
    }
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        engine2D.dispose()
    }
}