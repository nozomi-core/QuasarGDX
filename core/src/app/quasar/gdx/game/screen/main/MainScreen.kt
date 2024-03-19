package app.quasar.gdx.game.screen.main

import app.quasar.gdx.localisation.LocalString
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport


class MainScreen(
    private val spriteBatch: SpriteBatch
): Screen {

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport

    private val img by lazy { Texture("benchmark.png") }

    override fun show() {
        camera = OrthographicCamera()
        viewport = ExtendViewport(320f, 180f, camera)

        val prop =  LocalString.entry_level_naming

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        viewport.apply()
        camera.update()
        spriteBatch.projectionMatrix = camera.combined;
        spriteBatch.begin()
        spriteBatch.draw(img, 0f, 0f)
        spriteBatch.end()
    }

    override fun resize(width: Int, height: Int) {
       viewport.update(width, height)
    }
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        img.dispose()
    }
}