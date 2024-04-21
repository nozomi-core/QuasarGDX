package app.quasar.qgl.language

import app.quasar.qgl.engine.core.GameNode

class ProviderStack<out T: GameData>(private val startValue: T) {

    private val additions = mutableListOf<Pair<GameNode<*, *>, T>>()

    val size: Int
        get() = 1 + additions.size

    fun get(): T {
        return if(additions.isEmpty())
            return startValue
        else
            additions.last().second
    }

    fun push(node: GameNode<*, *>, value: @UnsafeVariance T) {

        val existingNode = additions.find { pair ->
            pair.first == pair
        }

        if(existingNode == null) {
            additions.add(Pair(node, value))
            //TODO: find solution node.providesInto(this, value)
        }
    }

    fun forEach(callback: (T) -> Unit) {
        callback(startValue)
        additions.forEach { callback(it.second) }
    }

    internal fun remove(gameNode: GameNode<*, *>) {
        additions.removeIf {
            it.first == gameNode
        }
    }
}

