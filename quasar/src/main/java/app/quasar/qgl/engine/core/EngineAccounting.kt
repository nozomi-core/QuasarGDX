package app.quasar.qgl.engine.core

class EngineAccounting(
    internal var runtimeGameId: Long
) {
    fun nextId(): Long = runtimeGameId++
}