package app.quasar.qgl.tiles

import app.quasar.qgl.engine.EngineApi
import kotlin.reflect.KClass

interface GameWorld {
    fun useRootScripts(): List<KClass<*>>
    fun onCreate(engine: EngineApi)
}