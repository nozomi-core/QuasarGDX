package app.quasar.gdx.tools.enginetest

import app.quasar.gdx.CoreAssets
import app.quasar.gdx.tiles.CoreTileset
import app.quasar.qgl.engine.CommonRuntime
import app.quasar.qgl.engine.Quasar2D
import app.quasar.qgl.engine.core.WindowScreen
import app.quasar.qgl.engine.serialize.ClassFactory
import app.quasar.qgl.tiles.GameWindow
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import java.io.File

class EngineTestApplication(
    private val runtime: CommonRuntime
): ApplicationAdapter() {

    private lateinit var worldCamera: OrthographicCamera
    private lateinit var overlayCamera: OrthographicCamera

    private lateinit var worldViewport: Viewport
    private lateinit var overlayViewport: Viewport
    private lateinit var engine2D: Quasar2D

    private val screen = WindowScreen(1920f, 1080f)

    private val window = object : GameWindow {
        override fun getWorldCamera() = worldCamera
        override fun getWorldViewport() = worldViewport
        override fun getOverlayCamera() = overlayCamera
        override fun getOverlayViewport() = overlayViewport
        override fun getWindow()= screen
    }

    override fun create() {
        super.create()
        worldCamera = OrthographicCamera()
        overlayCamera = OrthographicCamera()
        worldViewport = ExtendViewport(320f, 180f, worldCamera)
        overlayViewport = ExtendViewport(screen.width, screen.height, overlayCamera)

        engine2D = Quasar2D(
            window = window,
            textureFile = CoreAssets.Sprites.TILE_SET,
            tileset = CoreTileset(),
            tileSize = CoreAssets.TILE_SIZE,
            runtime = runtime,
            scriptFactory = ClassFactory(
                TestScripts,
                DataScripts
            )
        )
        createOrLoadWorld()
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

    private fun createOrLoadWorld() {
        val file = File("engine.qgl")
        if(file.exists()) {
            engine2D.loadWorld("engine.qgl")
        } else {
            engine2D.createWorld(EngineTestWorld::class)
        }
    }
}