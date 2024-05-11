package app.quasar.qgl.engine.core

/**
 * Maintains a safe list of nodes when keeping references to them outside the engine context
 * this is to ensure that when they are destroyed, references are cleaned up
 */
class NodeCollection1<T> {

    private val nodes = mutableListOf<NodeReference1<T>>()

    fun add(value: T) {
        nodes.add(NodeReference1(value))
    }

    fun remove(value: T) {
        nodes.removeIf { it.get() == value }
    }

    fun forEach(callback: (NodeReference1<T>) -> Unit) {
        clean()
        nodes.forEach(callback)
    }

    fun sortWith(comparator: Comparator<NodeReference1<T>>) {
        nodes.sortWith(comparator)
    }

    private fun clean() {
        nodes.removeIf { it.get() == null }
    }
}