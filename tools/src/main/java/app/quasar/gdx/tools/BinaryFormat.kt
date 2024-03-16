package app.quasar.gdx.tools

import kotlin.reflect.KClass

object BinaryFormat {
    private val binaryClasses = mutableListOf<KClass<*>>()

    init {

    }

    fun extend(list: List<KClass<*>>, resolver: (id: Int) -> KClass<*>?) {
        binaryClasses.addAll(list)
    }
}