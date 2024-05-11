package app.quasar.qgl.engine.core1.interfaces

interface WorldBounded1: WorldPosition1 {
    fun onBoundExceeded()
}