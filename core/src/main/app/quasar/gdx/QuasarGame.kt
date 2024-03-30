package app.quasar.gdx

import app.quasar.gdx.ui.main.MainScreen
import app.quasar.gdx.lumber.Lumber
import app.quasar.gdx.lumber.setupLumber
import app.quasar.qgl.QuasarRuntime
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class QuasarGame(
    private val runtime: QuasarRuntime,
    private val gameConfig: QuasarConfig
): Game() {

    private val spriteBatch by lazy { SpriteBatch() }


    override fun create() {
        //Setup core game modules here
        useLumber()

        //set starting screen for launcher
        startMainMenu()
        Lumber.debug("QusarGame :${Thread.currentThread().name}")
    }

    private fun useLumber() = setupLumber {
        isDebug = gameConfig.isDebug
    }

    private fun startMainMenu() {
        setScreen(MainScreen(runtime))
    }

    override fun dispose() {
        spriteBatch.dispose()
    }
}