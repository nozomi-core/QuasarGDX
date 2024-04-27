package app.quasar.qgl.engine.core

class NodeProvider<T> {

    private var default: T? = null
    private val nodeSet = HashSet<NodeReference<T>>()

    private fun cleanCollectedReferences() {
        nodeSet.removeIf {
            it.get() == null
        }
    }

    fun provide(): T? {
        cleanCollectedReferences()
        return if(nodeSet.isEmpty()) {
            default
        } else {
            nodeSet.last().get()
        }
    }

    fun setDefault(default: T) {
        this.default = default
    }

    fun push(value: T) {
        nodeSet.add(NodeReference(value))
    }

    fun pop(node: T) {
        nodeSet.removeIf { it.get() == node }
    }
}