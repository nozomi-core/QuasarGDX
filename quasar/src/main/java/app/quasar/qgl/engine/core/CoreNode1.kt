package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

interface CoreNode1 {
    fun getShouldRunBefore(): List<KClass<*>>
    fun onCoreCreated()
}