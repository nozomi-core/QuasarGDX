package app.quasar.qgl.render

import app.quasar.qgl.engine.core.ProjectionApi
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

class ProjectionApiActual(
    private val world: Camera,
    private val overlay: Camera
): ProjectionApi {

    override fun screenToWorld(screenVector: Vector3): Vector3 {
        return world.unproject(screenVector)
    }

    override fun worldToScreen(worldVector: Vector3): Vector3 {
        return world.project(worldVector)
    }

    override fun overlayToScreen(overlayVector: Vector3): Vector3 {
        return overlay.project(overlayVector)
    }

    override fun screenToOverlay(screenVector: Vector3): Vector3 {
        return overlay.unproject(screenVector)
    }
}