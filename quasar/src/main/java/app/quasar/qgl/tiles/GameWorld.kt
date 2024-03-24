package app.quasar.qgl.tiles

import app.quasar.qgl.engine.EngineApi

interface GameWorld {
    fun onCreateRoot(engine: EngineApi)
    fun onCreate(engine: EngineApi)
}