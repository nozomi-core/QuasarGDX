package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.TilemapData
import app.quasar.gdx.tools.model.Grid
import app.quasar.gdx.tools.model.createRandomTileInfo
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

@QGLEntity("tilemap")
class TilemapScript: GameNode<TilemapData>() {
    private val tileSize = 16

    private val grid = Grid(tileSize, 100, 100, 0f,  0f)

    override fun onCreate(argument: NodeArgument): TilemapData {
        return TilemapData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: TilemapData) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            data.tiles.set(2,2, 1)
        }
    }

    override fun onDraw(context: DrawContext, data: TilemapData) {
        for(x in context.minWorldX until context.maxWorldX step tileSize) {
            for(y in context.minWorldY until context.maxWorldY step tileSize) {

                val gridX = x / tileSize
                val gridY = y / tileSize

                if(gridX >= 0 && gridY >= 0) {
                    val tileId = data.tiles.get(gridX, gridY)
                    val sprite = getView(tileId)


                    grid.getLocation(gridX, gridY)?.let {
                        context.draw.tilePx(sprite, it.x, it.y)
                    }
                }
            }
        }
    }

    private fun getView(id: Int): SpriteId {
        return when(id) {
            0 -> CoreTiles.SIGNAL_CLOSE
            1 -> CoreTiles.SIGNAL_REGULAR
            else -> CoreTiles.TRANSPARENT
        }
    }
}