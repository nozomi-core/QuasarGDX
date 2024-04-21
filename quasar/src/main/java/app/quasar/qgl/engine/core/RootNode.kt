package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

abstract class RootNode<D, A>: GameNode<D, A>() {

    open fun shouldRunBefore(): List<KClass<*>> = emptyList()

    protected open fun onRootCreated(){}

    internal fun doRootCreated() {
        onRootCreated()
    }
}