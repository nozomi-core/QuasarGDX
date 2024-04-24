package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.engine.core.EngineClock
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.GameNodeApi
import app.quasar.qgl.scripts.EngineLogger
import app.quasar.qgl.scripts.InputFocus
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class ConsolePrinterScript: GameNode<Unit, Unit>(), ConsolePrinter {

    private lateinit var inputFocus: InputFocus

    override fun onCreate(argument: Unit?) {}
    override fun onSetup(engine: EngineApi, data: Unit?) {
        inputFocus = engine.requireFindByInterface(InputFocus::class)
    }

    override fun onSimulate(engine: EngineApi, node: GameNodeApi, clock: EngineClock, data: Unit)
    {
        inputFocus.withInputFocus(this) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                val engineLogger = engine.requireFindByInterface(EngineLogger::class)

                engineLogger.message(this, "ENTER THE CONSOLE!!")
                inputFocus.popInput(this)
            }

            if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
                engine.createGameNode(Keyboard::class)
            }
        }
    }

    override fun takeOverInput() {
        inputFocus.pushInput(this)
    }
}

interface ConsolePrinter {
    fun takeOverInput()
}