package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

interface CoreNode {
    fun getShouldRunBefore(): List<KClass<*>>
    fun onCoreCreated()
}