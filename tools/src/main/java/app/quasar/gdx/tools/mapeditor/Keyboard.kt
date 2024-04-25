package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.scripts.EngineLogger
import app.quasar.qgl.scripts.InputFocus
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Keyboard: GameNodeUnit() {
    private lateinit var logger: EngineLogger
    private lateinit var inputFocus: InputFocus

    override fun onSetup(context: SetupContext, data: Unit?) {
        logger = context.engine.requireFindByInterface(EngineLogger::class)
        inputFocus = context.engine.requireFindByInterface(InputFocus::class)

        logger.message(this, "KeyBoard.create")
        inputFocus.pushInput(this)
    }

    override fun onSimulate(context: SimContext, self: SelfContext, data: Unit) {
        inputFocus.withInputFocus(this) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                logger.message(this, "UNABLE!!")
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                self.destroyNode()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.message(this, "KeyBoard.destroy")
        inputFocus.popInput(this)
    }
}