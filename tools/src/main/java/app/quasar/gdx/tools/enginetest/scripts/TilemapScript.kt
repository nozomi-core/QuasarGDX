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

    private val grid = Grid(16, 2048, 2048, 0f,  0f)

    override fun onCreate(argument: NodeArgument): TilemapData {
        return TilemapData()
    }

    override fun onDraw(context: DrawContext, data: TilemapData) {
        for(x in context.minWorldX until context.maxWorldX step 16) {
            for(y in context.minWorldY until context.maxWorldY step 16) {

                grid.getGridForPixel(x.toFloat(), y.toFloat())?.let {
                    context.draw.tilePx(CoreTiles.RED_DARK, it.x, it.y)
                }
            }
        }
    }
}