package app.quasar.qgl.scripts

import app.quasar.qgl.engine.core.GameNodeUnit
import app.quasar.qgl.engine.core.NodeProvider
import app.quasar.qgl.engine.core.SelfContext
import app.quasar.qgl.engine.core.SimContext
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter

interface InputStack {
    fun setDefault(node: InputNode)
    fun pushInput(node: InputNode)
    fun popInput(node: InputNode)
    fun withInputFocus(node: InputNode, callback: () -> Unit)
}

class InputStackScript: GameNodeUnit(), InputStack {

    private val provider = NodeProvider<InputNode>()

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

    override fun onSimulate(context: SimContext, self: SelfContext, data: Unit) {
        val inputFocus = provider.provide()
        Gdx.input.inputProcessor = inputFocus?.getInputAdapter()
    }
}

interface InputNode {
    fun getInputAdapter(): InputAdapter?
}