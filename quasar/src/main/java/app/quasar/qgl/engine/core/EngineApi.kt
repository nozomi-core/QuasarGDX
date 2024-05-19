package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

interface EngineApi {
    fun setDimension(dimension: EngineDimension)
    fun <T: GameNode<*>> createNode(dimension: EngineDimension, script: KClass<T>, factory: (NodeFactory) -> Unit = {})
    fun queryNodeByTag(tag: String): NodeReference<ReadableGameNode>?
    fun queryAll(): List<NodeReference<ReadableGameNode>>
}