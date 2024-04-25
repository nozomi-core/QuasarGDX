package app.quasar.qgl.engine.core.interfaces

interface WorldBounded: WorldPosition {
    fun onBoundExceeded()
}