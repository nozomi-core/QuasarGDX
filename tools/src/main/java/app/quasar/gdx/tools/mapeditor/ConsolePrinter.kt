package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.scripts.EngineLogger
import app.quasar.qgl.scripts.InputFocus
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class ConsolePrinterScript: GameNode<Unit, Unit>(), ConsolePrinter {

    private lateinit var inputFocus: InputFocus

    override fun onCreate(argument: Unit?) {}
    override fun onSetup(context: SetupContext, data: Unit?) {
        inputFocus = context.engine.requireFindByInterface(InputFocus::class)
    }

    override fun onSimulate(context: SimContext, node: SelfContext, data: Unit) {
        val engine = context.engine

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