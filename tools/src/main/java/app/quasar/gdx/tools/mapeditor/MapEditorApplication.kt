package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.CoreAssets
import app.quasar.gdx.tiles.CoreTileset
import app.quasar.qgl.QuasarRuntime
import app.quasar.qgl.tiles.QuasarEngine2DConfig
import app.quasar.qgl.tiles.UiHooks
import app.quasar.qgl.engine.Quasar2DEngine
import app.quasar.qgl.engine.core.OverlayScreen
import app.quasar.qgl.tiles.TileSheetLayout
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport

class MapEditorApplication(private val runtime: QuasarRuntime): ApplicationAdapter() {

    private lateinit var worldCamera: OrthographicCamera
    private lateinit var overlayCamera: OrthographicCamera

    private lateinit var worldViewport: Viewport
    private lateinit var overlayViewport: Viewport
    private lateinit var engine2D: Quasar2DEngine

    private val screen = OverlayScreen(1920f, 1080f)

    private val config by lazy {
        QuasarEngine2DConfig(
            texture = Texture(CoreAssets.Sprites.TILE_SET),
            spriteBatch = SpriteBatch(),
            tileset = CoreTileset(),
            layout = TileSheetLayout(tileSize = 16),
            screen = screen
        )
    }

    private val uiHooks = object : UiHooks {
        override fun useWorldCamera() = worldCamera
        override fun useWorldViewport() = worldViewport
        override fun useOverlayCamera() = overlayCamera
        override fun useOverlayViewport() = overlayViewport
    }

    override fun create() {
        super.create()
        engine2D = Quasar2DEngine(runtime, config, uiHooks).apply {
            createWorld(EditWorld::class)
        }
        worldCamera = OrthographicCamera()
        overlayCamera = OrthographicCamera()
        worldViewport = ExtendViewport(320f, 180f, worldCamera)
        overlayViewport = ExtendViewport(screen.width, screen.height, overlayCamera)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        worldViewport.update(width, height)
        overlayViewport.update(width, height)
    }

    override fun render() {
        super.render()
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        engine2D.render()
    }

    override fun dispose() {
        super.dispose()
        engine2D.dispose()
    }
}