package app.quasar.qgl.engine.core

import java.lang.ref.WeakReference

class NodeProvider<T> {

    private var default: T? = null
    private val nodeSet = HashSet<WeakReference<T>>()

    private fun cleanCollectedReferences() {
        nodeSet.removeIf { it.get() == null }
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
        nodeSet.add(WeakReference(value))
    }

    fun pop(node: T) {
        nodeSet.removeIf { it.get() == node }
    }
}