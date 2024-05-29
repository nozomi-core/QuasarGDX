package app.quasar.qgl.engine.core

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class DrawContext(
    val draw: DrawableApi
) {
    var minWorldX = 0
        private set
    var minWorldY = 0
        private set
    var maxWorldX = 0
        private set
    var maxWorldY = 0
        private set

    private val worldRect = Rectangle()
    private var query = Vector3()

    fun inside(vector3: Vector3): Boolean = worldRect.contains(vector3.x, vector3.y)

    //Set the current draw context window so engine knows what sections of the world it should draw
    fun update(screen: WindowScreen, project: ProjectionApi) {
        query.x = 0f - RENDER_BUFFER
        query.y = screen.height + RENDER_BUFFER
        project.screenToWorld(query)

        minWorldX = query.x.toInt()
        minWorldY = query.y.toInt()

        query.x = screen.width + RENDER_BUFFER
        query.y = 0f - RENDER_BUFFER
        project.screenToWorld(query)
        maxWorldX = query.x.toInt()
        maxWorldY = query.y.toInt()

        worldRect.set(minWorldX.toFloat(), minWorldY.toFloat(), (maxWorldX - minWorldX).toFloat(), (maxWorldY -  minWorldY).toFloat())
    }
    companion object {
        //The amount of pixels off-screen that should be rendered
        const val RENDER_BUFFER = 128f
    }
}

class SimContext(
    val engine: EngineApi,
    val clock: EngineClock,
    val project: ProjectionApi,
    val camera: CameraApi
)