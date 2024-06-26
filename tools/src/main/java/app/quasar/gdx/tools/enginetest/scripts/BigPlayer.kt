package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.PlayerData
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector3

@QGLEntity("big_player")
class BigPlayer: GameNode<PlayerData>() {
    override fun onCreate(argument: NodeArgument): PlayerData {
        return PlayerData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: PlayerData) {
        super.onSimulate(self, context, data)

        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            context.engine.replace(this, PlayerScript::class)
        }
    }

    override fun onDraw(context: DrawContext, data: PlayerData) {
        context.draw.tilePx(CoreTiles.SMILE, Vector3(), 4f, 0f)
    }
}