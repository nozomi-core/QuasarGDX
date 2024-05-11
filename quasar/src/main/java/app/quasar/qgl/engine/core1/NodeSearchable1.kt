package app.quasar.qgl.engine.core1

import app.quasar.qgl.engine.core1.interfaces.WorldPosition1
import kotlin.reflect.KClass

interface NodeSearchable1 {
    fun <T: Any> requireFindByInterface(nodeInterface: KClass<T>): T

    //TODO: create test for when finding by ID, and it exists but does not implement the interface
    fun <T: Any> findById(id: Long, nodeInterface: KClass<T>): T?
    fun <T: Any> forEachInterface(nodeInterface: KClass<T>, callback: (T) -> Unit)
    fun <T: Any> getNearby(target: WorldPosition1, distance: Float, nodeInterface: KClass<T>): List<T>
}