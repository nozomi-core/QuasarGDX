package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.CoreAssets
import app.quasar.gdx.tiles.CoreTileset
import app.quasar.qgl.QuasarRuntime
import app.quasar.qgl.engine.QuasarEngine2DConfig
import app.quasar.qgl.engine.EngineHooks
import app.quasar.qgl.engine.Quasar2DEngine
import app.quasar.qgl.tiles.TileSheetLayout
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class MapEditorApplication(private val runtime: QuasarRuntime): ApplicationAdapter() {

    private lateinit var worldCamera: OrthographicCamera
    private lateinit var overlayCamera: OrthographicCamera

    private lateinit var worldViewport: Viewport
    private lateinit var overlayViewport: Viewport
    private lateinit var engine2D: Quasar2DEngine

    private val config by lazy {
        QuasarEngine2DConfig(
            texture = Texture(CoreAssets.TILE_SET),
            spriteBatch = SpriteBatch(),
            tileset = CoreTileset(),
            layout = TileSheetLayout(tileSize = 16)
        )
    }

    private val engineCallbacks = object : EngineHooks {
        override fun useWorldCamera() = worldCamera
        override fun useWorldViewport() = worldViewport
        override fun useOverlayCamera() = overlayCamera
        override fun useOverlayViewport() = overlayViewport
    }

    override fun create() {
        super.create()
        engine2D = Quasar2DEngine(runtime, config, engineCallbacks).apply {
            createWorld(EditWorld::class)
            createOverlay(EditOverlay::class)
        }
        worldCamera = OrthographicCamera()
        overlayCamera = OrthographicCamera()
        worldViewport = ExtendViewport(320f, 180f, worldCamera)
        overlayViewport = FitViewport(400f, 400f, overlayCamera)
        overlayCamera.translate(200f, 200f)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        worldViewport.update(width, height)
        overlayViewport.update(width, height)
    }

    override fun render() {
        super.render()
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

    override fun dispose() {
        super.dispose()
        engine2D.dispose()
    }
}