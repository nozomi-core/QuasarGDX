package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.EngineHooks
import kotlin.reflect.KClass

//The public API for the engine that is safe to expose to the game nodes
interface EngineApi: NodeSearchable {
    fun getEngineHooks(): EngineHooks
    fun generateId(): Long
    fun <T: GameNode<*, *>> createGameNode(node: KClass<T>, argument: Any? = null)
}