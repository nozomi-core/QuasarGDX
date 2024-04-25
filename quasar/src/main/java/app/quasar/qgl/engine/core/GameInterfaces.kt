package app.quasar.qgl.engine.core

import com.badlogic.gdx.math.Vector3

interface GameOverlay {
    fun onDrawOverlay(context: DrawContext)
}

interface WorldBounded: WorldPosition {
    fun onBoundExceeded()
}

interface WorldPosition {
    fun query(input: Vector3)
}