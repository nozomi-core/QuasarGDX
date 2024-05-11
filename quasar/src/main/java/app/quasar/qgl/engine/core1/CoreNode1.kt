package app.quasar.qgl.engine.core1

import kotlin.reflect.KClass

interface CoreNode1 {
    fun getShouldRunBefore(): List<KClass<*>>
    fun onCoreCreated()
}