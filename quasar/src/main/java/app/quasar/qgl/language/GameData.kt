package app.quasar.qgl.language

import app.quasar.qgl.engine.EngineApi

abstract class GameData(engineApi: EngineApi) {
    val runtimeId: Long = engineApi.generateId()
}