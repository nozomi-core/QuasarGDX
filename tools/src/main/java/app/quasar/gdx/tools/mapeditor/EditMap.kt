package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.QuasarTiles
import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.tiles.GameWorld

class EditMap: GameWorld {

    private var positionX = 0f
    private val speedMs = 4f

    override fun onCreate() {
        //TODO("Not yet implemented")
    }

    override fun onSimulate(delta: Float) {
        positionX += speedMs * delta
    }

    override fun onDraw(draw: DrawableApi) {
        //Draw map
        for(x in 0 until 20) {
            for(y in 0 until 20) {
                val evenX = x % 2 == 0
                val evenY = y % 2 == 0

                if(evenX && evenY) {
                    draw.tileGrid(QuasarTiles.GREEN_LIGHT, x, y)
                } else {
                    draw.tileGrid(QuasarTiles.RED_LIGHT, x, y)
                }
            }
        }

        draw.tilePx(QuasarTiles.RED_DARK, positionX, 0f)
    }
}