package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.WorldPosition
import kotlin.reflect.KClass

interface NodeSearchable {
    fun <T: Any> requireFindByInterface(nodeInterface: KClass<T>): T

    //TODO: create test for when finding by ID, and it exists but does not implement the interface
    fun <T: Any> findById(id: Long, nodeInterface: KClass<T>): T?
    fun <T: Any> forEachInterface(nodeInterface: KClass<T>, callback: (T) -> Unit)
    fun <T: Any> getNearby(target: WorldPosition,distance: Float, nodeInterface: KClass<T>): List<T>
}