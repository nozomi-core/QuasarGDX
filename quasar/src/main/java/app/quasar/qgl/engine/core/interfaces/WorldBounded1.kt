package app.quasar.qgl.engine.core.interfaces

interface WorldBounded1: WorldPosition1 {
    fun onBoundExceeded()
}