package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector3

class PlayerScript: GameNode<Unit>() {

    private val position = Vector3(0f, 0f,0f)

    override fun onCreate(argument: NodeArgument) {}

    override fun onSimulate(self: SelfContext, context: SimContext, data: Unit) {
        val clock = context.clock
        val speed = 50f

        if(Gdx.input.isKeyPressed(Keys.W)) {
            position.y += clock.mulDeltaTime(speed)
        }
        if(Gdx.input.isKeyPressed(Keys.A)) {
            position.x -= clock.mulDeltaTime(speed)
        }
        if(Gdx.input.isKeyPressed(Keys.S)) {
            position.y -= clock.mulDeltaTime(speed)
        }
        if(Gdx.input.isKeyPressed(Keys.D)) {
            position.x += clock.mulDeltaTime(speed)
        }

        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            context.engine.createNode(MissileScript::class) {
                it.argument = AnyNodeArgument(position.cpy())
            }
        }
    }

    override fun onDraw(context: DrawContext, data: Unit) {
        context.draw.tilePx(CoreTiles.SMILE, position.x, position.y)
        context.camera.setCamera(position.x, position.y)
    }
}