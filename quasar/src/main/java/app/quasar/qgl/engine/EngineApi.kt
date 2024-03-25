package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import kotlin.reflect.KClass

//The public API for the engine that is safe to expose to the game nodes
interface EngineApi {
    fun generateId(): Long
    fun <T: GameNode> createGameNode(node: KClass<T>, argument: Any? = null)

    fun <T: Any> requireFindByInterface(node: KClass<T>): T
    fun <T: Any> findById(id: Long, type: KClass<T>): T?

    //Finds the nearest node after this one (Create stack like architecture)
    /*
    fun <T: Any> findNearestAfter(node: GameNode, lookFor: KClass<T>): T?
    fun <T: Any> findNearestBefore(node: GameNode, lookFor: KClass<T>): T?*/
}