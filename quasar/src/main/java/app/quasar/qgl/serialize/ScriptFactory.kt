package app.quasar.qgl.serialize

import app.quasar.qgl.engine.core.GameNode
import kotlin.reflect.KClass

class ScriptBuilder {
    private val scripts = mutableListOf<ScriptDef>()

    fun <G : GameNode<D>, D> add(id: Int, kClass: KClass<G>, mapper: QGLMapper<D>) {
        scripts.add(ScriptDef(id, kClass, mapper))
    }

    fun getDefinitionById(id: Int): ScriptDef? = scripts.find { it.id == id }
    fun getDefinitionByClass(kClass: KClass<*>): ScriptDef? = scripts.find { it.kClass == kClass }

    fun applyScripts(factory: ScriptFactory) {
        factory.callback(this)
    }
}

typealias ScriptCallback = ScriptBuilder.() -> Unit

interface ScriptFactory {
    val callback: ScriptCallback
}

class ScriptDef(val id: Int, val kClass: KClass<*>, val mapper: QGLMapper<*>)