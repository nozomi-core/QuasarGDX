package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

class NodeGraph: NodeSearchable {
    private val nodes = mutableListOf<GameNode<*, *>>()
    private val callbacks = mutableSetOf<GraphChangedListener>()

    fun addListener(listener: GraphChangedListener) {
        callbacks.add(listener)
    }

    fun remove(node: GameNode<*,*>) {
        nodes.remove(node)
        callbacks.forEach { it.onRemoved(node) }
    }

    fun add(node: GameNode<*,*>) {
        nodes.add(node)
        callbacks.forEach { it.onAdded(node) }
    }

    fun forEach(consumer: (GameNode<*,*>) -> Unit) = nodes.forEach(consumer)
    fun first(predicate: (GameNode<*,*>) -> Boolean) = nodes.first(predicate)
    fun indexOf(node: GameNode<*,*>) = nodes.indexOf(node)

    override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
        checkCastIsInterface(nodeInterface)
        val first =  nodes.firstOrNull { nodeInterface.java.isAssignableFrom(it.javaClass)}
        return first as T!!
    }

    override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
        checkCastIsInterface(nodeInterface)
        val first = nodes.first { it.runtimeId == id && it.isAlive }
        return first as? T?
    }
}

private fun checkCastIsInterface(kClass: KClass<*>) {
    if(!kClass.java.isInterface) {
        throw SecurityException("Can only search for interfaces when finding a node in the engine")
    }
}

interface GraphChangedListener {
    fun onAdded(node: GameNode<*,*>)
    fun onRemoved(node: GameNode<*,*>)
}