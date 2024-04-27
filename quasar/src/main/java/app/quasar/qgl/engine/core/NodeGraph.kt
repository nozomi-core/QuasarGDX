package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.WorldPosition
import com.badlogic.gdx.math.Vector3
import kotlin.reflect.KClass

class NodeGraph: NodeSearchable {
    private val nodes = mutableListOf<GameNode<*>>()
    private val callbacks = mutableSetOf<GraphChangedListener>()

    val size: Int get() = nodes.size

    fun addListener(listener: GraphChangedListener) {
        callbacks.add(listener)
    }

    fun remove(node: GameNode<*>) {
        nodes.remove(node)
        callbacks.forEach { it.onRemoved(node) }
    }

    fun add(node: GameNode<*>) {
        nodes.add(node)
        callbacks.forEach { it.onAdded(node) }
    }

    fun contains(script: KClass<*>): Boolean {
        return nodes.find { it::class == script } != null
    }

    fun forEach(consumer: (GameNode<*>) -> Unit) = nodes.forEach(consumer)
    fun first(predicate: (GameNode<*>) -> Boolean) = nodes.first(predicate)
    fun indexOf(node: GameNode<*>) = nodes.indexOf(node)

    override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
        checkCastIsInterface(nodeInterface)
        val first =  nodes.firstOrNull { it.isImplemented(nodeInterface) }
        return first as T!!
    }

    override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
        checkCastIsInterface(nodeInterface)
        val first = nodes.first { it.id == id && it.isAlive }
        return first as? T?
    }

    override fun <T : Any> forEachInterface(nodeInterface: KClass<T>, callback: (T) -> Unit) {
        nodes.forEach { node ->
            if(node.isImplemented(nodeInterface)) {
                callback(node as T)
            }
        }
    }

    override fun <T : Any> getNearby(target: WorldPosition, distance: Float, nodeInterface: KClass<T>): List<T> {
        val result = mutableListOf<T>()
        val targetVector = Vector3().apply { target.query(this) }
        val checkVector = Vector3()

        nodes.forEach { node ->
            if(node.isImplemented(nodeInterface) && node is WorldPosition) {
                //check distance
                node.query(checkVector)
                if(targetVector.dst(checkVector) < distance) {
                    result.add(node as T)
                }
            }
        }
        return result
    }

    private fun GameNode<*>.isImplemented(nodeInterface: KClass<*>): Boolean {
        return nodeInterface.java.isAssignableFrom(this.javaClass)
    }

    private fun checkCastIsInterface(kClass: KClass<*>) {
        if(!kClass.java.isInterface) {
            throw SecurityException("Can only search for interfaces when finding a node in the engine")
        }
    }
}

interface GraphChangedListener {
    fun onAdded(node: GameNode<*>)
    fun onRemoved(node: GameNode<*>)
}