package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.DrawableApi
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeApi
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector3

class Player: GameNode<Unit, Unit>() {

    private var position = Vector3(0f,0f,0f)

    override fun onCreate(argument: Unit?) {}

    override fun onSimulate(node: NodeApi, deltaTime: Float, data: Unit) {
        val worldCamera = node.engine.getEngineHooks().useWorldCamera()

        // Move the camera based on input events
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x += -SPEED_MS * deltaTime
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += SPEED_MS * deltaTime
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += SPEED_MS * deltaTime
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y += -SPEED_MS * deltaTime
        }
        worldCamera.position.x = position.x
        worldCamera.position.y = position.y
    }

    override fun onDraw(draw: DrawableApi) {
        super.onDraw(draw)
        draw.tilePx(CoreTiles.GREEN_LIGHT, position.x, position.y)
    }

    companion object {
        const val SPEED_MS = 20
    }
}