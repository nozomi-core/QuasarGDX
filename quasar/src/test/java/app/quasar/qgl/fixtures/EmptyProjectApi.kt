package app.quasar.qgl.fixtures

import app.quasar.qgl.engine.core.ProjectionApi
import com.badlogic.gdx.math.Vector3

class EmptyProjectApi: ProjectionApi {
    override fun unprojectWorld(screenVector: Vector3): Vector3 {
        return Vector3()
    }
}