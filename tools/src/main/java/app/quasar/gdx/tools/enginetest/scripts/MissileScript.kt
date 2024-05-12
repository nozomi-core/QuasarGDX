package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*

class MissileScript: GameNode<Unit>() {

    private var xPos: Float = 0f

    override fun onCreate(argument: NodeArgument) {}

    override fun onSimulate(self: SelfContext, context: SimContext, data: Unit) {
        xPos += 1f
    }

    override fun onDraw(context: DrawContext, data: Unit) {
        context.draw.tilePx(CoreTiles.SMILE, xPos,0f)
    }
}