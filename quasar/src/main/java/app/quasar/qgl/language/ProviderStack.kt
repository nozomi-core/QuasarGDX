package app.quasar.qgl.language

import app.quasar.qgl.entity.GameNode

class ProviderStack<T>(private val startValue: T) {

    private val additions = mutableListOf<Pair<GameNode, T>>()

    fun get(): T {
        return if(additions.isEmpty())
            return startValue
        else
            additions.last().second
    }

    fun push(node: GameNode, value: T) {
        val existingNode = additions.find { pair ->
            pair.first == pair
        }

        if(existingNode == null) {
            additions.add(Pair(node, value))
            node.providesInto(this, value)
        }
    }

    internal fun remove(gameNode: GameNode) {
        additions.removeIf {
            it.first == gameNode
        }
    }
}

