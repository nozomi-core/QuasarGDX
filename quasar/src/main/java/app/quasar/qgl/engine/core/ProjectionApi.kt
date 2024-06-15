package app.quasar.qgl.engine.core

import com.badlogic.gdx.math.Vector3

interface ProjectionApi {
    fun screenToWorld(screenVector: Vector3): Vector3
    fun worldToScreen(worldVector: Vector3): Vector3
    fun overlayToScreen(overlayVector: Vector3): Vector3
    fun screenToOverlay(screenVector: Vector3): Vector3
}