package app.quasar.qgl.engine.core.interfaces

import com.badlogic.gdx.math.Vector3

interface WorldPosition {
    fun query(input: Vector3): Vector3
}