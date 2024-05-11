package app.quasar.qgl.engine.core

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class NodeGraph {

    private val nodeList = mutableListOf<GameNode>()

    internal fun simulate() {
        nodeList.forEach {
            it.simulate()
        }
    }

    internal fun <T : GameNode> createNode(script: KClass<T>, factory: (NodeFactory) -> Unit) {
        val newNode = script.createInstance()

        newNode.create(NodeFactory(factory))
        nodeList.add(newNode)
    }

    internal fun findByTag(tag: String): ReadableGameNode? {
        return nodeList.find { it.tag == tag }
    }
}