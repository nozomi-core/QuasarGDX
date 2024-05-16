package app.quasar.qgl.render

import app.quasar.qgl.engine.core.ProjectionApi
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

class ProjectionApiActual(
    private val world: Camera
): ProjectionApi {

    override fun unprojectWorld(screenVector: Vector3): Vector3 {
        return world.unproject(screenVector)
    }
}