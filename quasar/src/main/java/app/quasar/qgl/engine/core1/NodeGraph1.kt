package app.quasar.qgl.engine.core1

import app.quasar.qgl.engine.core1.interfaces.WorldPosition1
import com.badlogic.gdx.math.Vector3
import kotlin.reflect.KClass

class NodeGraph1: NodeSearchable1 {
    private val nodes = mutableListOf<GameNode1<*>>()
    private val callbacks = mutableSetOf<GraphChangedListener1>()

    val size: Int get() = nodes.size

    fun addListener(listener: GraphChangedListener1) {
        callbacks.add(listener)
    }

    fun remove(node: GameNode1<*>) {
        nodes.remove(node)
        callbacks.forEach { it.onRemoved(node) }
    }

    fun add(node: GameNode1<*>) {
        nodes.add(node)
        callbacks.forEach { it.onAdded(node) }
    }

    fun contains(script: KClass<*>): Boolean {
        return nodes.find { it::class == script } != null
    }

    fun forEach(consumer: (GameNode1<*>) -> Unit) = nodes.forEach(consumer)
    fun first(predicate: (GameNode1<*>) -> Boolean) = nodes.first(predicate)
    fun indexOf(node: GameNode1<*>) = nodes.indexOf(node)

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

    override fun <T : Any> getNearby(target: WorldPosition1, distance: Float, nodeInterface: KClass<T>): List<T> {
        val result = mutableListOf<T>()
        val targetVector = Vector3().apply { target.query(this) }
        val checkVector = Vector3()

        nodes.forEach { node ->
            if(node.isImplemented(nodeInterface) && node is WorldPosition1) {
                //check distance
                node.query(checkVector)
                if(targetVector.dst(checkVector) < distance) {
                    result.add(node as T)
                }
            }
        }
        return result
    }

    private fun GameNode1<*>.isImplemented(nodeInterface: KClass<*>): Boolean {
        return nodeInterface.java.isAssignableFrom(this.javaClass)
    }

    private fun checkCastIsInterface(kClass: KClass<*>) {
        if(!kClass.java.isInterface) {
            throw SecurityException("Can only search for interfaces when finding a node in the engine")
        }
    }
}

interface GraphChangedListener1 {
    fun onAdded(node: GameNode1<*>)
    fun onRemoved(node: GameNode1<*>)
}