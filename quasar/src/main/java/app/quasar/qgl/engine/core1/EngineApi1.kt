package app.quasar.qgl.engine.core1

import kotlin.reflect.KClass

//The public API for the engine that is safe to expose to the game nodes
interface EngineApi1: NodeSearchable1 {
    fun generateId(): Long
    fun <T: GameNode1<*>> createNode(node: KClass<T>, argument: Any? = null)
    fun <T: GameNode1<*>> createSingleNode(node: KClass<T>, argument: Any? = null)
}