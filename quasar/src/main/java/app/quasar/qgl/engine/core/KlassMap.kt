package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

interface KlassFactory {
    fun add(id: Int, kClass: KClass<*>)
}

class KlassMap: KlassFactory {

    private val classMap = mutableMapOf<Int, KClass<*>>()

    override fun add(id: Int, kClass: KClass<*>) {
        classMap[id] = kClass
    }
    fun apply(factory: (KlassFactory) -> Unit) {
        factory(this)
    }

    fun get(id: Int): KClass<*> = classMap[id]!!
}