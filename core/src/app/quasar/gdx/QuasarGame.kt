package app.quasar.gdx

import app.quasar.gdx.lumber.Lumber
import app.quasar.gdx.lumber.LumberConfig
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class QuasarGame(
    private val gameConfig: QuasarConfig
): ApplicationAdapter() {

    private val batch by lazy { SpriteBatch() }
    private val img by lazy { Texture("badlogic.jpg") }

    override fun create() {
        super.create()
        setupLumber()
    }

    private fun setupLumber() {
        Lumber.setup(
            LumberConfig(isDebug = gameConfig.isDebug)
        )
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        batch.begin()
        batch.draw(img, 0f, 0f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}