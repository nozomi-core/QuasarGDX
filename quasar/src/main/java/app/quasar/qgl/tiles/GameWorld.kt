package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.EngineApi1
import kotlin.reflect.KClass

abstract class GameWorld {
    abstract fun useRootScripts(): List<KClass<*>>
    open fun onCreate(engine: EngineApi1) {}

    internal fun create(engine: EngineApi1) {
        onCreate(engine)
    }
}