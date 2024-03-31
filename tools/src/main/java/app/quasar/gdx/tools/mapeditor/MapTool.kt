package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.QuasarTiles
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.render.DrawableApi

data class MapToolArg(val startX: Float, val startY: Float)

class MapTool: GameNode() {

    private var timer: Float = 0f

    private var xPosition: Float = 0f

    override fun onSimulate(deltaTime: Float) {
        timer += deltaTime
        if(timer >= 10) {
            destroyNode()
        }

        xPosition += deltaTime * 12f
    }

    override fun onDraw(drawableApi: DrawableApi) {
        with(drawableApi) {
            tilePx(QuasarTiles.GREEN_LIGHT, xPosition, -32f)
        }
    }

    override fun onCreate(argument: Any?) {
        //val parent = engineApi.requireFindByInterface(PublicEditMap::class)
       // parent.printMessage("Hey there")

        createChild(ToolRender::class, null)
    }

    override fun onDestroy() {
       //engineApi?.createGameNode(GreenTile::class, MapToolArg(xPosition, -10f))
    }
}

class ToolRender: GameNode() {
    override fun onCreate(argument: Any?) {
        //TODO("Not yet implemented")
    }

    override fun onSimulate(deltaTime: Float) {
        //TODO("Not yet implemented")
    }

    override fun onDraw(drawableApi: DrawableApi) {
        drawableApi.tileGrid(QuasarTiles.RED_DARK, -10, -10)
    }
}