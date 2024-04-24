package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.DrawableApi
import app.quasar.qgl.engine.core.EngineClock
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeApi
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3

class Player: GameNode<Unit, Unit>() {

    private var position = Vector3(0f,0f,0f)
    private var rotate: Float = 0f

    override fun onCreate(argument: Unit?) {}

    override fun onSimulate(node: NodeApi, clock: EngineClock, data: Unit) {
        val worldCamera = node.engine.getEngineHooks().useWorldCamera()

        // Move the camera based on input events
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x += clock.multiply(-PLAYER_SPEED)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += clock.multiply(PLAYER_SPEED)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += clock.multiply(PLAYER_SPEED)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y += clock.multiply(-PLAYER_SPEED)
        }
        worldCamera.position.x = position.x
        worldCamera.position.y = position.y

        //calc rotation
        rotate += clock.multiply(ROTATE_SPEED)
    }

    override fun onDraw(draw: DrawableApi) {
        super.onDraw(draw)
        draw.batchWith { api ->
            api.setColor(Color.CYAN)
            draw.tilePx(CoreTiles.GREEN_LIGHT, position.x, position.y, 1f, rotate)
        }

    }

    companion object {
        const val PLAYER_SPEED = 20f
        const val ROTATE_SPEED = 1000f
    }
}