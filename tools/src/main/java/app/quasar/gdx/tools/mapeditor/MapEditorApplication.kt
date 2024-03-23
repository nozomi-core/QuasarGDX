package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.QuasarTileset
import app.quasar.qgl.engine.QuasarCallbacks
import app.quasar.qgl.engine.QuasarEngine2D
import app.quasar.qgl.tiles.GameTileset
import app.quasar.qgl.tiles.TileSheetLayout
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport


class MapEditorApplication: ApplicationAdapter() {

    private val texture by lazy { Texture("badlogic.jpg") }
    private val tileTexture by lazy { Texture("sprites/tileset.png") }
    private val spriteBatch by lazy { SpriteBatch() }
    private val gameTileset = QuasarTileset()

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var engine2D: QuasarEngine2D

    private val engineCallbacks = object: QuasarCallbacks {
        override fun getTexture() = tileTexture
        override fun getSpriteBatch() = spriteBatch
        override fun getGameTileset() = gameTileset
        override fun getCamera() = camera
        override fun getTileLayout() = TileSheetLayout(
            tileSize = 16,
            sheetWidthPx = texture.width,
            sheetHeightPx = texture.height
        )
    }

    override fun create() {
        super.create()
        engine2D = QuasarEngine2D(engineCallbacks).apply {
            setWorld(EditMap())
        }
        camera = OrthographicCamera()
        viewport = ExtendViewport(400f, 400f, camera)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height)
    }

    override fun render() {
        super.render()
        handleInput()
        ScreenUtils.clear(1f, 1f, 1f, 1f)
        viewport.apply()
        camera.update()
        engine2D.render()
    }

    private fun handleInput() {
        // Move the camera based on input events
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-1f, 0f, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(1f, 0f, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0f, 1f, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0f, -1f, 0f)
        }
    }

    override fun dispose() {
        super.dispose()
        texture.dispose()
    }
}