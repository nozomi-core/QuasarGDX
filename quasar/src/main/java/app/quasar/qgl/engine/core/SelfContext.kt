package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

interface SelfContext {
    fun <T: GameNode<*>> spawnChild(dimension: EngineDimension, script: KClass<T>, factory: (NodeFactory) -> Unit = {})
    fun setDimension(dimension: EngineDimension)
    fun destroy()
}