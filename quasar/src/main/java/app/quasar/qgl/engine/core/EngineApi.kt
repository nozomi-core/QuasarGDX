package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

interface EngineApi {
    val current: EngineDimension
    fun generateId(): Long
    fun setDimension(dimension: EngineDimension)
    fun <T: GameNode<*>> createNode(dimension: EngineDimension, script: KClass<T>, factory: (NodeFactory) -> Unit = {})
    fun <T: GameNode<D>, D> replace(node: GameNode<D>, replaceScript: KClass<T>)
    fun queryNodeByTag(tag: String): NodeReference<ReadableGameNode>?
    fun queryAll(): List<NodeReference<ReadableGameNode>>
}