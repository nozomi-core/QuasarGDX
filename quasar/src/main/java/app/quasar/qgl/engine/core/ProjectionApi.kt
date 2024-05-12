package app.quasar.qgl.engine.core

import com.badlogic.gdx.math.Vector3

interface ProjectionApi {
    fun unprojectWorld(screenVector: Vector3): Vector3
}