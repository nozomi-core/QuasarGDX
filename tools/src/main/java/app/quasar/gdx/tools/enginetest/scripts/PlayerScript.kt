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
import com.badlogic.gdx.audio.Sound

@QGLEntity("player")
class PlayerScript: VectorNode<PlayerData>(), WorldPosition {

    private lateinit var sound: Sound

    override fun onCreate(argument: NodeArgument): PlayerData {
        return PlayerData()
    }

    override fun onAttach(self: SelfContext, engine: EngineApi, data: PlayerData) {
        // Load the sound file
        sound = Gdx.audio.newSound(Gdx.files.internal("music/park_ambient.wav"));

        // Play the sound
        sound.play(1.0f);
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: PlayerData) {
        val clock = context.clock
        val speed = 50f

        data.rotation += clock.mulDeltaTime(data.rotateSpeed)

        if(Gdx.input.isKeyJustPressed(Keys.TAB)) {
            context.engine.setDimension(EngineTestWorld.DessertDimen)
            context.engine.createNode(selfDimension, DimensionTransition::class)
            self.setDimension(EngineTestWorld.DessertDimen)
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.P)) {
            context.engine.setDimension(EngineTestWorld.MainDimen)
            self.setDimension(EngineTestWorld.MainDimen)
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