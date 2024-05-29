package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.PlayerData
import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.engine.core.SelfContext
import app.quasar.qgl.engine.core.SimContext
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import app.quasar.qgl.engine.core.model.VectorNode
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class ChildScript: VectorNode<PlayerData>(), WorldPosition {

    override fun onCreate(argument: NodeArgument): PlayerData {
        position.x = 100f
        position.y = 100f
        return PlayerData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: PlayerData) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            self.spawnChild(selfDimension, ChildChildScript::class)
        }
    }

    override fun onDraw(context: DrawContext, data: PlayerData) {
        context.draw.tilePx(CoreTiles.GREEN_LIGHT, position.x, position.y)
    }
}