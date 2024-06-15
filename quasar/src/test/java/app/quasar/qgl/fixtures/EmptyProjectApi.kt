package app.quasar.qgl.fixtures

import app.quasar.qgl.engine.core.ProjectionApi
import com.badlogic.gdx.math.Vector3

class EmptyProjectApi: ProjectionApi {
    override fun screenToWorld(screenVector: Vector3): Vector3 {
        return Vector3()
    }

    override fun worldToScreen(worldVector: Vector3): Vector3 {
        return Vector3()
    }

    override fun screenToOverlay(screenVector: Vector3): Vector3 {
        return Vector3()
    }
}