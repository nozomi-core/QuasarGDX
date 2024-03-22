package app.quasar.gdx

import app.quasar.gdx.game.screen.main.MainScreen
import app.quasar.gdx.lumber.setupLumber
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class QuasarGame(
    private val gameConfig: QuasarConfig
): Game() {

    private val spriteBatch by lazy { SpriteBatch() }


    override fun create() {
        //Setup core game modules here
        useLumber()

        //set starting screen for launcher
        startMainMenu()
    }

    private fun useLumber() = setupLumber {
        isDebug = gameConfig.isDebug
    }

    private fun startMainMenu() {
        setScreen(MainScreen(spriteBatch))
    }

    override fun dispose() {
        spriteBatch.dispose()
    }
}