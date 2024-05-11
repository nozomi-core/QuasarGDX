package app.quasar.qgl.engine.core1

import kotlin.reflect.KClass

interface SelfContext1: NodeSearchable1 {
    fun destroyNode()
    fun <T: GameNode1<*>> createChild(node: KClass<T>, argument: Any? = null)
    fun <T: GameNode1<*>> createSingleChild(node: KClass<T>, argument: Any? = null)
    fun getParent(): GameNode1<*>
}