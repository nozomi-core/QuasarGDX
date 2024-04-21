package app.quasar.gdx

import app.quasar.gdx.ui.main.MainScreen
import app.quasar.qgl.QuasarRuntime
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class CoreGame(
    private val runtime: QuasarRuntime,
    private val gameConfig: CoreConfig
): Game() {

    private val spriteBatch by lazy { SpriteBatch() }


    override fun create() {
        //set starting screen for launcher
        startMainMenu()
    }

    private fun startMainMenu() {
        setScreen(MainScreen(runtime))
    }

    override fun dispose() {
        spriteBatch.dispose()
    }
}