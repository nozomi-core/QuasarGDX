package app.quasar.qgl.entity

import kotlin.reflect.KClass

class NodeGraph: NodeSearchable {
    val gameNodes = mutableListOf<GameNode<*, *>>()

    override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
        checkCastIsInterface(nodeInterface)
        val first =  gameNodes.firstOrNull { nodeInterface.java.isAssignableFrom(it.javaClass)}
        return first as T!!
    }

    override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
        checkCastIsInterface(nodeInterface)
        val first = gameNodes.first { it.runtimeId == id && it.isAlive }
        return first as T?
    }
}

private fun checkCastIsInterface(kClass: KClass<*>) {
    if(!kClass.java.isInterface) {
        throw SecurityException("Can only search for interfaces when finding a node in the engine")
    }
}