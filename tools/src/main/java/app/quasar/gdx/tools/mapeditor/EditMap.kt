package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.QuasarTiles
import app.quasar.qgl.render.DrawApi
import app.quasar.qgl.tiles.GameMap

class EditMap: GameMap {
    override fun onCreate() {
        //TODO("Not yet implemented")
    }

    override fun onDraw(draw: DrawApi) {
        for(x in 0 until 20) {
            for(y in 0 until 20) {
                val evenX = x % 2 == 0
                val evenY = y % 2 == 0

                if(evenX && evenY) {
                    draw.tile(QuasarTiles.GREEN_LIGHT, x, y)
                } else {
                    draw.tile(QuasarTiles.RED_LIGHT, x, y)
                }
            }
        }
    }
}