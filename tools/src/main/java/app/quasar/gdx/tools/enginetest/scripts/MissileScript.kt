package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.MissileData
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector3

@QGLEntity("missile")
class MissileScript: GameNode<MissileData>(), WorldPosition {

    var time = 0f

    override fun onCreate(argument: NodeArgument): MissileData {
        return when(argument) {
            is AnyNodeArgument -> MissileData().apply {
                position = argument.value as Vector3
            }
            else -> throw creationException()
        }
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: MissileData) {
        if(Gdx.input.isKeyJustPressed(Keys.Q)) {
            self.spawnChild (selfDimension, ChildScript::class)
        }

        data.position.x += context.clock.mulDeltaTime(30f)

        time += context.clock.deltaTime

        if(time > 20) {
            self.destroy()
        }
    }

    override fun onDraw(context: DrawContext, data: MissileData) {
        context.draw.tilePx(CoreTiles.SMILE, data.position.x, data.position.y)
    }

    override fun queryPosition(input: Vector3): Vector3 {
        return input.set(requireForInterface.position)
    }
}