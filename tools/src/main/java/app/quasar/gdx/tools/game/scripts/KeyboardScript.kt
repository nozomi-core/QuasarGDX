package app.quasar.gdx.tools.game.scripts

import app.quasar.qgl.engine.core1.*
import app.quasar.qgl.scripts.ConsoleLog
import app.quasar.qgl.scripts.InputNode
import app.quasar.qgl.scripts.InputStack
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter

interface Keyboard: InputNode

class KeyboardScript: GameNodeUnit1(), Keyboard {
    private lateinit var logger: ConsoleLog
    private lateinit var inputFocus: InputStack

    private lateinit var inputAdapter: InputAdapter

    private var stringBuilder = StringBuilder()

    override fun onSetup(context: SetupContext1, data: Unit) {
        logger = context.engine.requireFindByInterface(ConsoleLog::class)
        inputFocus = context.engine.requireFindByInterface(InputStack::class)

        logger.message(this, "KeyBoard.create")
        inputFocus.pushInput(this)

        inputAdapter = object : InputAdapter() {
            override fun keyTyped(character: Char): Boolean {
                if(character == '\n') {
                    logger.message(this, stringBuilder.toString())
                    stringBuilder = StringBuilder()
                }
                stringBuilder.append(character)
                return true
            }
        }
    }

    override fun onSimulate(context: SimContext1, self: SelfContext1, data: Unit) {
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

    override fun getInputAdapter(): InputAdapter = inputAdapter
}