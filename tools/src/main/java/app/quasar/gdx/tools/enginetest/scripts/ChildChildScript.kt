package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.PlayerData
import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.engine.core.SelfContext
import app.quasar.qgl.engine.core.SimContext
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import app.quasar.qgl.engine.core.model.VectorNode

class ChildChildScript: VectorNode<PlayerData>(), WorldPosition {

    override fun onCreate(argument: NodeArgument): PlayerData {
        position.x = 132f
        position.y = 132f
        return PlayerData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: PlayerData) {

    }

    override fun onDraw(context: DrawContext, data: PlayerData) {
        context.draw.tilePx(CoreTiles.TREE, position.x, position.y)
    }
}