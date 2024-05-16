package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

interface EngineApi {
    fun <T: GameNode<*>> createNode(script: KClass<T>, factory: (NodeFactory) -> Unit = {})
    fun findNodeByTag(tag: String): NodeReference<ReadableGameNode>?
}