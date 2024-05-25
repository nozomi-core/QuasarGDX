package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.PlayerData
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector3

@QGLEntity("player")
class PlayerScript: GameNode<PlayerData>(), WorldPosition {

    override fun queryPosition(input: Vector3): Vector3 = input.set(requireForInterface.position)

    override fun onCreate(argument: NodeArgument): PlayerData {
        return PlayerData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: PlayerData) {
        val clock = context.clock
        val speed = 50f

        data.rotation += clock.mulDeltaTime(data.rotateSpeed)

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
            context.engine.createNode(selfDimension, MissileScript::class) {
                it.argument = AnyNodeArgument(data.position.cpy())
            }
        }

        if(Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
            context.engine.setDimension(EngineDimension(1))
            self.setDimension(EngineDimension(1))
        }

        if(Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
            context.engine.setDimension(EngineDimension(2))
            self.setDimension(EngineDimension(2))
        }

        if(Gdx.input.isKeyPressed(Keys.SPACE)) {
            data.rotation += clock.mulDeltaTime(10f)
        }

        if(Gdx.input.isKeyPressed(Keys.L)) {
            data.isRotating = !data.isRotating
        }

        if(Gdx.input.isKeyPressed(Keys.EQUALS)) {
            data.rotateSpeed += 5
        }

        if(Gdx.input.isKeyPressed(Keys.MINUS)) {
            data.rotateSpeed -= 5
        }

        if(Gdx.input.isKeyJustPressed(Keys.R)) {
            context.engine.replace(this, BigPlayer::class)
        }

        context.camera.setCamera(data.position.x, data.position.y)
    }

    override fun onDraw(context: DrawContext, data: PlayerData) {
        context.draw.tilePx(CoreTiles.SMILE, data.position.x, data.position.y, 1f, data.rotation)
    }
}