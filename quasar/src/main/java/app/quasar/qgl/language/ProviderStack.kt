package app.quasar.qgl.language

import app.quasar.qgl.entity.GameNode

class ProviderStack<T>(private val startValue: T) {

    private val additions = mutableListOf<Pair<Long, T>>()

    val size: Int
        get() = 1 + additions.size

    fun get(): T {
        return if(additions.isEmpty())
            return startValue
        else
            additions.last().second
    }

    fun push(node: GameNode, value: T) {
        val existingNode = additions.find { pair ->
            pair.first  == node.runtimeId
        }

        if(existingNode == null) {
            additions.add(Pair(node.runtimeId, value))
            node.providesInto(this, value)
        }
    }

    fun forEach(callback: (T) -> Unit) {
        callback(startValue)
        additions.forEach { callback(it.second) }
    }

    internal fun remove(gameNode: GameNode) {
        additions.removeIf {
            it.first == gameNode.runtimeId
        }
    }
}

