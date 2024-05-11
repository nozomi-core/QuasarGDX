package app.quasar.gdx.ui.main

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.DrawContext1
import app.quasar.qgl.engine.core.GameNodeUnit1

class MainMap: GameNodeUnit1() {

    override fun onDraw(context: DrawContext1, data: Unit) {
        val draw = context.draw

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
}