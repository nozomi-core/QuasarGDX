package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.scripts.EngineLogger
import app.quasar.qgl.scripts.InputNode
import app.quasar.qgl.scripts.InputStack
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter

class ConsolePrinterScript: GameNodeUnit(), ConsolePrinter, InputNode {

    private lateinit var inputFocus: InputStack

    override fun onSetup(context: SetupContext, data: Unit) {
        inputFocus = context.engine.requireFindByInterface(InputStack::class)
    }

    override fun onSimulate(context: SimContext, self: SelfContext, data: Unit) {
        val engine = context.engine

        inputFocus.withInputFocus(this) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                val engineLogger = engine.requireFindByInterface(EngineLogger::class)

                engineLogger.message(this, "ENTER THE CONSOLE!!")
                inputFocus.popInput(this)
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                engine.createGameNode(Keyboard::class)
            }
        }
    }

    override fun takeOverInput() {
        inputFocus.pushInput(this)
    }

    override fun getInputAdapter(): InputAdapter? = null
}

interface ConsolePrinter {
    fun takeOverInput()
}