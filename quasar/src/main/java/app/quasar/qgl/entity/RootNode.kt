package app.quasar.qgl.entity

import kotlin.reflect.KClass

open class RootNode: GameNode() {

    open fun shouldRunBefore(): List<KClass<*>> = emptyList()

    protected open fun onRootCreated(){}

    internal fun doRootCreated() {
        onRootCreated()
    }
}