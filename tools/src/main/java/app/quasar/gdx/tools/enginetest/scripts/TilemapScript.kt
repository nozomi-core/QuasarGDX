package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.TilemapData
import app.quasar.gdx.tools.model.Grid
import app.quasar.gdx.tools.model.createRandomTileInfo
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.GameOverlay
import app.quasar.qgl.engine.core.interfaces.ShapeOverlay
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3

@QGLEntity("tilemap")
class TilemapScript: GameNode<TilemapData>(), GameOverlay, ShapeOverlay {
    private val tileSize = 16

    private val grid = Grid(tileSize, 100, 100, 0f,  0f)

    override fun onCreate(argument: NodeArgument): TilemapData {
        return TilemapData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: TilemapData) {
        if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
            try {
                val mouse = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                val world = context.project.screenToWorld(mouse)

                val gridX = (world.x / tileSize).toInt()
                val gridY = (world.y / tileSize).toInt()

                data.tiles.set(gridX, gridY, 1)
            }catch (e: Exception) {

            }


        }

        if(Gdx.input.isKeyPressed(Keys.END)) {
            self.destroy()
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

    override fun onDrawOverlay(context: DrawContext) {
        val proj = Vector3()
        proj.x = Gdx.input.x.toFloat()
        proj.y = Gdx.input.y.toFloat()

        context.project.screenToOverlay(proj)

        context.draw.tilePx(CoreTiles.RED_DARK, proj.x, proj.y, 5f, 0f)
    }

    override fun onShape(shape: ShapeApi) {
        val draw = shape.getRender()

        draw.color = Color.LIGHT_GRAY
        draw.rect(300f,0f, 400f, 400f)
    }
}