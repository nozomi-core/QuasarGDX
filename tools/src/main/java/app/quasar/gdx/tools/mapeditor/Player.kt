package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.scripts.InputFocus
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3

class Player: GameNode<Unit, Unit>() {

    private var position = Vector3(0f,0f,0f)
    private var rotate: Float = 0f

    private lateinit var inputFocus: InputFocus

    override fun onCreate(argument: Unit?) {}

    override fun onSetup(context: SetupContext, data: Unit?) {
        inputFocus = context.engine.requireFindByInterface(InputFocus::class)
        inputFocus.setDefault(this)
    }

    override fun onSimulate(context: SimContext, self: SelfContext, data: Unit) {
        val engine = context.engine
        val clock = context.clock

        val worldCamera = context.engine.getEngineHooks().useWorldCamera()

        worldCamera.position.x = position.x
        worldCamera.position.y = position.y
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
            renderPriority++
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            renderPriority--
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)) {
            val printer = engine.requireFindByInterface(ConsolePrinter::class)
            printer.takeOverInput()
        }
    }

    override fun onDraw(context: DrawContext) {
        val draw = context.draw

        draw.batchWith { api ->
            api.setColor(Color.CYAN)
            draw.tilePx(CoreTiles.SMILE, position.x, position.y, 1f, rotate)
        }
    }

    companion object {
        const val PLAYER_SPEED = 50f
        const val ROTATE_SPEED = 100f
    }


}