package app.quasar.gdx.game.screen.main

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class MainScreen(
    private val spriteBatch: SpriteBatch
): Screen {

    private val img by lazy { Texture("badlogic.jpg") }

    override fun show() {

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        spriteBatch.begin()
        spriteBatch.draw(img, 0f, 0f)
        spriteBatch.end()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        img.dispose()
    }
}