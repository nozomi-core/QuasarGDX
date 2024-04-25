package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.GameNode

interface PublicEditMap {
    fun printMessage(msg: String)
}

class EditMap: GameNode<Unit, Unit>(), PublicEditMap {

    private var positionX = 0f

    private val speedMs = 4f

    override fun onDraw(context: DrawContext, data: Unit) {
        val draw = context.draw

        //Draw map
        for(x in 0 until 20) {
            for(y in 0 until 20) {
                val evenX = x % 2 == 0
                val evenY = y % 2 == 0

                if(evenX && evenY) {
                    draw.batchWith { spriteApi ->
                        spriteApi.setAlpha(0.5f)
                        draw.tileGrid(CoreTiles.GREEN_LIGHT, x, y)
                    }


                } else {
                    draw.tileGrid(CoreTiles.RED_LIGHT, x, y)
                }
            }
        }

        draw.tilePx(CoreTiles.RED_DARK, positionX, 0f)
    }

    override fun printMessage(msg: String) {
        println("Using public message interface: $msg")
    }

    override fun onCreate(argument: Unit?) {

    }
}