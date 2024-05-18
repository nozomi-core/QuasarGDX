package app.quasar.qgl.engine.core

class EngineAccounting {
    private var runtimeGameId: Long = 10000

    fun nextId(): Long = runtimeGameId++
}