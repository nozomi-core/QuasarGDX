package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.PlayerData
import app.quasar.gdx.tools.enginetest.mapper.PlayerMapper
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.serialize.QGLMapper
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector3

class PlayerScript: GameNode<PlayerData>() {

    override fun onCreate(argument: NodeArgument): PlayerData {
        return PlayerData(position = Vector3(0f, 0f,0f), 0f)
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: PlayerData) {
        val clock = context.clock
        val speed = 50f

        if(Gdx.input.isKeyPressed(Keys.W)) {
            data.position.y += clock.mulDeltaTime(speed)
        }
        if(Gdx.input.isKeyPressed(Keys.A)) {
            data.position.x -= clock.mulDeltaTime(speed)
        }
        if(Gdx.input.isKeyPressed(Keys.S)) {
            data.position.y -= clock.mulDeltaTime(speed)
        }
        if(Gdx.input.isKeyPressed(Keys.D)) {
            data.position.x += clock.mulDeltaTime(speed)
        }

        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            context.engine.createNode(MissileScript::class) {
                it.argument = AnyNodeArgument(data.position.cpy())
            }
        }

        if(Gdx.input.isKeyPressed(Keys.SPACE)) {
            data.rotation += clock.mulDeltaTime(10f)
        }

        context.camera.setCamera(data.position.x, data.position.y)
    }

    override fun onDraw(context: DrawContext, data: PlayerData) {
        context.draw.tilePx(CoreTiles.SMILE, data.position.x, data.position.y, 1f, data.rotation)
    }
}