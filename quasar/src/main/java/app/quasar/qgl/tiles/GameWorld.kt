package app.quasar.qgl.tiles

import app.quasar.qgl.engine.EngineApi
import kotlin.reflect.KClass

abstract class GameWorld {
    abstract fun useRootScripts(): List<KClass<*>>
    open fun onCreate(engine: EngineApi) {}

    internal fun create(engine: EngineApi) {
        onCreate(engine)
    }
}