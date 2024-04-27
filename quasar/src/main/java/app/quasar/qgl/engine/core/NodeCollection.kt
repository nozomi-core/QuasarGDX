package app.quasar.qgl.engine.core

/**
 * Maintains a safe list of nodes when keeping references to them outside the engine context
 * this is to ensure that when they are destroyed, references are cleaned up
 */
class NodeCollection<T> {

    private val nodes = mutableListOf<NodeReference<T>>()

    fun add(value: T) {
        nodes.add(NodeReference(value))
    }

    fun remove(value: T) {
        nodes.removeIf { it.get() == value }
    }

    fun forEach(callback: (NodeReference<T>) -> Unit) {
        clean()
        nodes.forEach(callback)
    }

    fun sortWith(comparator: Comparator<NodeReference<T>>) {
        nodes.sortWith(comparator)
    }

    private fun clean() {
        nodes.removeIf { it.get() == null }
    }
}