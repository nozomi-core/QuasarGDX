package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.TilemapData
import app.quasar.gdx.tools.model.Grid
import app.quasar.gdx.tools.model.createRandomTileInfo
import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.engine.core.SpriteId
import app.quasar.qgl.serialize.QGLEntity

@QGLEntity("tilemap")
class TilemapScript: GameNode<TilemapData>() {

    private val grid = Grid(16, 100, 100, 0f,  0f)

    override fun onCreate(argument: NodeArgument): TilemapData {
        return TilemapData().apply {
            tiles = createRandomTileInfo(100, 100).toMutableList()
        }
    }

    override fun onDraw(context: DrawContext, data: TilemapData) {
       grid.forEach {
           context.draw.tilePx(CoreTiles.RED_DARK, it.x, it.y)
       }

        data.tiles.forEach { info ->
            val gridPlace = grid.getLocation(info.rowX, info.columnY)
            context.draw.tilePx(SpriteId.find(info.spriteId)!!, gridPlace!!.x, gridPlace.y)
        }
    }
}