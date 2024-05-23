package app.quasar.qgl.engine.serialize

import app.quasar.qgl.engine.core.GameData
import app.quasar.qgl.engine.core.GameNode
import kotlin.reflect.KClass

class ClassFactory(
    val scriptBuilder: ScriptFactory,
    val dataBuilder: DataFactory
)

class ScriptBuilder {
    private val scripts = mutableListOf<KClass<*>>()

    fun <G : GameNode<*>> add(kClass: KClass<G>) {
        scripts.add(kClass)
    }

    fun applyScripts(factory: ScriptFactory) {
        factory.scripts(this)
    }
}

class DataBuilder {
    private val scripts = mutableListOf<KClass<*>>()

    fun <G : GameData> add(kClass: KClass<G>) {
        scripts.add(kClass)
    }
}

typealias ScriptCallback = ScriptBuilder.() -> Unit
typealias DataCallback = DataBuilder.() -> Unit

interface ScriptFactory {
    val scripts: ScriptCallback
}

interface DataFactory {
    val data: DataCallback
}