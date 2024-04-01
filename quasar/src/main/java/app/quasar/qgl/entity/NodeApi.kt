package app.quasar.qgl.entity

import kotlin.reflect.KClass

interface NodeApi: NodeSearchable {
    fun destroyNode()
    fun <T: GameNode<*, *>> createChild(node: KClass<T>, argument: Any?)
}