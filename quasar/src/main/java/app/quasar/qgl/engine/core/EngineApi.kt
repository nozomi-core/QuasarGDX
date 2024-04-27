package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

//The public API for the engine that is safe to expose to the game nodes
interface EngineApi: NodeSearchable {
    fun generateId(): Long
    fun <T: GameNode<*>> createNode(node: KClass<T>, argument: Any? = null)
    fun <T: GameNode<*>> createSingleNode(node: KClass<T>, argument: Any? = null)
}