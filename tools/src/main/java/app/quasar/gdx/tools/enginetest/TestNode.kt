package app.quasar.gdx.tools.enginetest

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument

class TestNode: GameNode<Unit>() {
    override fun onCreate(argument: NodeArgument) {}

    override fun onDraw(context: DrawContext, data: Unit) {
        context.drawableApi.tilePx(CoreTiles.SMILE, 0f,0f)
    }
}