package app.quasar.qgl.tiles

import app.quasar.qgl.engine.core.EngineApi

abstract class GameWorld {
    open fun onCreate(engine: EngineApi) {}

    internal fun create(engine: EngineApi) {
        onCreate(engine)
    }
}