package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.MissileData
import app.quasar.gdx.tools.enginetest.mapper.MissileMapper
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.serialize.QGLMapper
import com.badlogic.gdx.math.Vector3

class MissileScript: GameNode<MissileData>() {

    override fun onCreate(argument: NodeArgument): MissileData {
        return when(argument) {
            is AnyNodeArgument -> MissileData(
                position = argument.value as Vector3
            )
            else -> throw creationException()
        }
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: MissileData) {
        data.position.x += context.clock.mulDeltaTime(30f)
    }

    override fun onDraw(context: DrawContext, data: MissileData) {
        context.draw.tilePx(CoreTiles.SMILE, data.position.x, data.position.y)
    }
}