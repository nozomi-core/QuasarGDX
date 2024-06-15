package app.quasar.qgl.engine.serialize

import app.quasar.qgl.engine.core.GameNode
import kotlin.reflect.KClass

class ClassFactory(
    val scriptFactory: ScriptFactory,
    val dataFactory: DataFactory
)

class ScriptBuilder {
    private val scripts = mutableSetOf<KClass<*>>()

    fun <G : GameNode<*>> add(kClass: KClass<G>) {
        scripts.add(kClass)
    }

    fun applyScripts(factory: ScriptFactory) {
        factory.scripts(this)
    }

    fun getList() = scripts.toList()
}

class DataBuilder {
    private val scripts = mutableSetOf<KClass<*>>()

    fun <G : Any> add(kClass: KClass<G>) {
        scripts.add(kClass)
    }

    fun applyScripts(factory: DataFactory) {
        factory.data(this)
    }

    fun getList() = scripts.toList()
}

typealias ScriptCallback = ScriptBuilder.() -> Unit
typealias DataCallback = DataBuilder.() -> Unit

interface ScriptFactory {
    val scripts: ScriptCallback
}

interface DataFactory {
    val data: DataCallback
}