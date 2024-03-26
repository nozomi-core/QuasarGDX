package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.QuasarTiles
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.render.DrawableApi

class GreenTile: GameNode() {

    private var xPos = 0f

    override fun onCreate(engineApi: EngineApi, argument: Any?) {
        when(argument) {
            is MapToolArg -> {
                xPos = argument.startX
            }
        }

    }
    override fun onSimulate(deltaTime: Float) {}

    override fun onDraw(drawableApi: DrawableApi) {
        drawableApi.tilePx(QuasarTiles.RED_DARK, xPos, -32f)
    }
}