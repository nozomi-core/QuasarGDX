package app.quasar.qgl.entity

import kotlin.reflect.KClass

interface NodeSearchable {
    fun <T: Any> requireFindByInterface(nodeInterface: KClass<T>): T
    fun <T: Any> findById(id: Long, nodeInterface: KClass<T>): T?
}