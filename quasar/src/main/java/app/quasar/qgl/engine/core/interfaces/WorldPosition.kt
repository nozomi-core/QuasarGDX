package app.quasar.qgl.engine.core.interfaces

import app.quasar.qgl.engine.core.model.StaticVector3
import com.badlogic.gdx.math.Vector3

 interface WorldPosition {
    fun queryPosition(input: Vector3): Vector3
}

interface StaticPosition {
    fun getPosition(): StaticVector3
}