package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.EngineTestWorld
import app.quasar.gdx.tools.enginetest.data.PlayerData
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import app.quasar.qgl.engine.core.model.VectorNode
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.Input.Keys

@QGLEntity("player")
class PlayerScript: VectorNode<PlayerData>(), WorldPosition {

    override fun onCreate(argument: NodeArgument): PlayerData {
        return PlayerData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: PlayerData) {
        val clock = context.clock
        val speed = 50f

        data.rotation += clock.mulDeltaTime(data.rotateSpeed)

        if(Gdx.input.isKeyJustPressed(Keys.TAB)) {
            self.setDimension(EngineTestWorld.DessertDimen)
            context.engine.setDimension(EngineTestWorld.DessertDimen)
            context.engine.createNode(selfDimension, DimensionTransition::class)
        }

        //TODO: fix issue that calling self.setDimensionAfter setting engine world means that this entity wont be drawn
        if(Gdx.input.isKeyJustPressed(Keys.P)) {
            self.setDimension(EngineTestWorld.MainDimen)
            context.engine.setDimension(EngineTestWorld.MainDimen)
            context.engine.createNode(selfDimension, DimensionTransition::class)
        }

        /*if(Gdx.input.isKeyPressed(Keys.W)) {
            position.y += clock.mulDeltaTime(speed)
        } */
        if(Gdx.input.isKeyPressed(Keys.A)) {
            position.x -= clock.mulDeltaTime(speed)
        }
        /*if(Gdx.input.isKeyPressed(Keys.S)) {
            position.y -= clock.mulDeltaTime(speed)
        }*/
        if(Gdx.input.isKeyPressed(Keys.D)) {
            position.x += clock.mulDeltaTime(speed)
        }

        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            context.engine.createNode(selfDimension, MissileScript::class) {
                it.argument = AnyNodeArgument(position.cpy())
            }
        }

        if(Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
            context.engine.setDimension(EngineDimension.create(1))
            self.setDimension(EngineDimension.create(1))
        }

        if(Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
            context.engine.setDimension(EngineDimension.create(2))
            self.setDimension(EngineDimension.create(2))
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

        if(Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            println("x: ${Gdx.input.x}, y: ${Gdx.input.y}")
        }

        context.camera.setCamera(position.x, position.y + (144 /2))
    }

    override fun onDraw(context: DrawContext, data: PlayerData) {
        context.draw.tilePx(CoreTiles.SMILE, position.x, position.y, 1f, data.rotation)
    }
}