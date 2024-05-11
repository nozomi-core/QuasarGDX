package app.quasar.gdx.tools.game.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.game.data.PlayerData
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.GameOverlay1
import app.quasar.qgl.engine.core.interfaces.WorldPosition1
import app.quasar.qgl.scripts.InputNode
import app.quasar.qgl.scripts.InputStack
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3
import kotlin.random.Random

interface Player: GameOverlay1, InputNode, WorldPosition1

class PlayerScript: GameNode1<PlayerData>(), Player {

    private lateinit var inputFocus: InputStack

    override fun onSetup(context: SetupContext1, data: PlayerData) {
        inputFocus = context.engine.requireFindByInterface(InputStack::class)
        inputFocus.setDefault(this)
        context.registerOverlay(this)
    }

    override fun onCreate(input: NodeInput1): PlayerData {
        return PlayerData(
            position = Vector3(),
            rotate = 2f
        )
    }

    override fun onSimulate(context: SimContext1, self: SelfContext1, data: PlayerData) {
        val engine = context.engine
        val clock = context.clock

        inputFocus.withInputFocus(this) {
            onHandleInput(data, context, self)
        }
        //calc rotation
        data.rotate += clock.multiply(ROTATE_SPEED)
    }

    private fun onHandleInput(data: PlayerData, context: SimContext1, self: SelfContext1) {
        // Move the camera based on input events
        val clock = context.clock
        val engine = context.engine

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            data.position.x += clock.multiply(-PLAYER_SPEED)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            data.position.x += clock.multiply(PLAYER_SPEED)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            data.position.y += clock.multiply(PLAYER_SPEED)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            data.position.y += clock.multiply(-PLAYER_SPEED)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            zDrawIndex++
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            zDrawIndex--
        }
        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            self.createSingleChild(FontScript::class,)
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            val random = Random(System.currentTimeMillis())
            val speed = random.nextFloat()

            MissileScript.create(engine, MissileInput(data.position, speed))
        }


        if(Gdx.input.isKeyPressed(Input.Keys.I)) {
            val printer = engine.requireFindByInterface(ConsolePrinter::class)
            printer.takeOverInput()
        }
    }

    override fun onDraw(context: DrawContext1, data: PlayerData) {
        val draw = context.draw

        draw.batchWith { api ->
            api.setColor(Color.CYAN)
            draw.tilePx(CoreTiles.SMILE, data.position.x, data.position.y, 1f, data.rotate)
        }

        context.camera.setCamera(data.position.x, data.position.y)
    }

    override fun onDrawOverlay(context: DrawContext1) {
        val draw = context.draw
    }

    override fun query(input: Vector3): Vector3 = input.set(dataForInterface.position)

    companion object {
        const val PLAYER_SPEED = 50f
        const val ROTATE_SPEED = 100f
    }

    override fun getInputAdapter(): InputAdapter? = null
}