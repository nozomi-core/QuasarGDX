package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

interface NodeApi: NodeSearchable {
    val engine: EngineApi

    fun destroyNode()
    fun <T: GameNode<*, *>> createChild(node: KClass<T>, argument: Any?)
    fun getParent(): GameNode<*, *>
}