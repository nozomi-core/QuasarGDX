package app.quasar.qgl.scripts

import app.quasar.qgl.engine.core.GameNodeUnit1
import app.quasar.qgl.engine.core.NodeProvider1
import app.quasar.qgl.engine.core.SelfContext1
import app.quasar.qgl.engine.core.SimContext1
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter

interface InputStack {
    fun setDefault(node: InputNode)
    fun pushInput(node: InputNode)
    fun popInput(node: InputNode)
    fun withInputFocus(node: InputNode, callback: () -> Unit)
}

class InputStackScript: GameNodeUnit1(), InputStack {

    private val provider = NodeProvider1<InputNode>()

    override fun setDefault(node: InputNode) {
       provider.setDefault(node)
    }

    override fun pushInput(node: InputNode) {
        provider.push(node)
    }

    override fun popInput(node: InputNode) {
        provider.pop(node)
    }

    override fun withInputFocus(node: InputNode, callback: () -> Unit) {
        if(provider.provide() == node) {
            callback()
        }
    }

    override fun onSimulate(context: SimContext1, self: SelfContext1, data: Unit) {
        val inputFocus = provider.provide()
        Gdx.input.inputProcessor = inputFocus?.getInputAdapter()
    }
}

interface InputNode {
    fun getInputAdapter(): InputAdapter?
}