package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.entity.NodeSearchable
import kotlin.reflect.KClass

//The public API for the engine that is safe to expose to the game nodes
interface EngineApi: NodeSearchable {
    fun generateId(): Long
    fun <T: GameNode<*, *>> createGameNode(node: KClass<T>, argument: Any? = null)
}