package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.engine.core.GameNodeApi
import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.engine.core.EngineClock
import app.quasar.qgl.engine.core.GameNodeUnit
import app.quasar.qgl.scripts.EngineLogger
import app.quasar.qgl.scripts.InputFocus
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Keyboard: GameNodeUnit() {
    private lateinit var logger: EngineLogger
    private lateinit var inputFocus: InputFocus

    override fun onSetup(engine: EngineApi, data: Unit?) {
        logger = engine.requireFindByInterface(EngineLogger::class)
        inputFocus = engine.requireFindByInterface(InputFocus::class)

        logger.message(this, "KeyBoard.create")
        inputFocus.pushInput(this)
    }

    override fun onSimulate(engine: EngineApi, node: GameNodeApi, clock: EngineClock, data: Unit) {
        inputFocus.withInputFocus(this) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                logger.message(this, "UNABLE!!")
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                node.destroyNode()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.message(this, "KeyBoard.destroy")
        inputFocus.popInput(this)
    }
}