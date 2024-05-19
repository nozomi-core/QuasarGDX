package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.engine.core.EngineDimension

abstract class GameWorld {
    open fun onCreate(engine: EngineApi): EngineDimension { return EngineDimension(0) }

    internal fun create(engine: EngineApi): EngineDimension {
        return onCreate(engine)
    }
}