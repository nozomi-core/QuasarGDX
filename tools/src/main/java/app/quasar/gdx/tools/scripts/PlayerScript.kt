package app.quasar.gdx.tools.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.GameOverlay
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import app.quasar.qgl.scripts.InputNode
import app.quasar.qgl.scripts.InputStack
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3
import kotlin.random.Random

class PlayerScript: GameNodeUnit(), Player {

    private var position = Vector3(0f,0f,0f)
    private var rotate: Float = 0f

    private lateinit var inputFocus: InputStack

    override fun onSetup(context: SetupContext, data: Unit) {
        inputFocus = context.engine.requireFindByInterface(InputStack::class)
        inputFocus.setDefault(this)
    }

    override fun onSimulate(context: SimContext, self: SelfContext, data: Unit) {
        val engine = context.engine
        val clock = context.clock

        inputFocus.withInputFocus(this) {
            onHandleInput(clock, engine)
        }
        //calc rotation
        rotate += clock.multiply(ROTATE_SPEED)
    }

    private fun onHandleInput(clock: EngineClock, engine: EngineApi) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            zDrawIndex++
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            zDrawIndex--
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            val random = Random(System.currentTimeMillis())
            val speed = random.nextFloat()

            MissileScript.create(engine, MissileInput(position, speed))
        }


        if(Gdx.input.isKeyPressed(Input.Keys.I)) {
            val printer = engine.requireFindByInterface(ConsolePrinter::class)
            printer.takeOverInput()
        }
    }

    override fun onDraw(context: DrawContext, data: Unit) {
        val draw = context.draw

        draw.batchWith { api ->
            api.setColor(Color.CYAN)
            draw.tilePx(CoreTiles.SMILE, position.x, position.y, 1f, rotate)
        }

        context.camera.setCamera(position.x, position.y)
    }

    override fun onDrawOverlay(context: DrawContext) {
        val draw = context.draw

        draw.tileGrid(CoreTiles.GREEN_LIGHT, 0, 0)
        draw.tileGrid(CoreTiles.RED_LIGHT, 0, 24)
        draw.tileGrid(CoreTiles.RED_LIGHT, 24, 24)
        draw.tileGrid(CoreTiles.RED_LIGHT, 24, 0)
        draw.tileGrid(CoreTiles.TREE, 0,0)
    }

    override fun query(input: Vector3): Vector3 = input.set(position)


    companion object {
        const val PLAYER_SPEED = 50f
        const val ROTATE_SPEED = 100f
    }

    override fun getInputAdapter(): InputAdapter? = null
}

interface Player: GameOverlay, InputNode, WorldPosition {

}