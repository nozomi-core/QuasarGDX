package app.quasar.qgl.engine.core

class EngineAccounting(
    private var runtimeGameId: Long
) {
    fun nextId(): Long = runtimeGameId++
}