package app.quasar.qgl.language

import app.quasar.qgl.entity.GameNode

class ProviderStack<out T: Providable>(private val startValue: T) {
    
    init {
        if(startValue::class.java.isAssignableFrom(GameNode::class.java)) {
            throw IllegalArgumentException("ProviderStack can not supply GameNode values from the engine, we do not support this, in general they should be plain data classes")
        }
    }

    private val additions = mutableListOf<Pair<GameNode, T>>()

    val size: Int
        get() = 1 + additions.size

    fun get(): T {
        return if(additions.isEmpty())
            return startValue
        else
            additions.last().second
    }

    fun push(node: GameNode, value: @UnsafeVariance T) {

        val existingNode = additions.find { pair ->
            pair.first == pair
        }

        if(existingNode == null) {
            additions.add(Pair(node, value))
            node.providesInto(this, value)
        }
    }

    fun forEach(callback: (T) -> Unit) {
        callback(startValue)
        additions.forEach { callback(it.second) }
    }

    internal fun remove(gameNode: GameNode) {
        additions.removeIf {
            it.first == gameNode
        }
    }
}

