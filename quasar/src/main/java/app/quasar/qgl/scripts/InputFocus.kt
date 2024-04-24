package app.quasar.qgl.scripts

import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.GameNodeUnit
import kotlin.collections.HashSet

class InpuStackScript: GameNodeUnit(), InputFocus {

    private var defaultFocus: Long = -1L
    private val inputStack = HashSet<Long>()

    override fun setDefault(gameNode: GameNode<*, *>) {
        defaultFocus = gameNode.runtimeId
    }

    override fun pushInput(gameNode: GameNode<*, *>) {
        inputStack.add(gameNode.runtimeId)
    }

    override fun popInput(gameNode: GameNode<*, *>) {
        inputStack.remove(gameNode.runtimeId)
    }

    override fun withInputFocus(node: GameNode<*, *>, callback: () -> Unit) {
        if(findNextHandlerId() == node.runtimeId) {
            callback()
        }
    }

    private fun findNextHandlerId(): Long {
        return if(inputStack.isEmpty()) {
            defaultFocus
        } else {
            inputStack.last()
        }
    }
}

interface InputFocus {
    fun setDefault(gameNode: GameNode<*,*>)
    fun pushInput(gameNode: GameNode<*, *>)
    fun popInput(gameNode: GameNode<*, *>)
    fun withInputFocus(node: GameNode<*,*>, callback: () -> Unit)
}