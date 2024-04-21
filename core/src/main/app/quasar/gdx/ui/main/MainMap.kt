package app.quasar.gdx.ui.main

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.DrawableApi

class MainMap: GameNode<Unit, Unit>() {

    override fun onDraw(draw: DrawableApi) {
        super.onDraw(draw)
        //Draw map
        for(x in 0 until 20) {
            for(y in 0 until 20) {
                val evenX = x % 2 == 0
                val evenY = y % 2 == 0

                if(evenX && evenY) {
                    draw.tileGrid(CoreTiles.GREEN_LIGHT, x, y)
                } else {
                    draw.tileGrid(CoreTiles.RED_LIGHT, x, y)
                }
            }
        }
    }

    override fun onCreate(argument: Unit?) {

    }
}