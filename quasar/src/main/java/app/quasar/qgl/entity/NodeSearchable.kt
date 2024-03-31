package app.quasar.qgl.entity

import kotlin.reflect.KClass

interface NodeSearchable {
    fun <T: Any> requireFindByInterface(nodeInterface: KClass<T>): T
    fun <T: Any> findById(id: Long, nodeInterface: KClass<T>): T?

    //Finds the nearest node after this one (Create stack like architecture)
    /*
    fun <T: Any> findNearestAfter(node: GameNode, lookFor: KClass<T>): T?
    fun <T: Any> findNearestBefore(node: GameNode, lookFor: KClass<T>): T?*/
}