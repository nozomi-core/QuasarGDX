package app.quasar.gdx.game.screen.main

import app.quasar.gdx.tiles.QuasarTiles
import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.render.DrawableApi

class MainMap: GameNode() {

    override fun onDraw(draw: DrawableApi) {
        super.onDraw(draw)
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
    }
}