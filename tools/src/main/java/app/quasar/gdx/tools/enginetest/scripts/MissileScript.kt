package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*
import com.badlogic.gdx.math.Vector3

class MissileScript: GameNode<Unit>() {

    private lateinit var position: Vector3

    override fun onCreate(argument: NodeArgument) {
        when(argument) {
            is AnyNodeArgument -> position = argument.value as Vector3
        }
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: Unit) {
        position.x += context.clock.mulDeltaTime(30f)
    }

    override fun onDraw(context: DrawContext, data: Unit) {
        context.draw.tilePx(CoreTiles.SMILE, position.x,position.y)
    }
}