package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.QuasarTiles
import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.tiles.GameOverlay

class EditOverlay: GameOverlay {

    override fun onCreate() {

    }

    override fun onDraw(draw: DrawableApi) {
        draw.tileGrid(QuasarTiles.GREEN_LIGHT, 0, 0)
        draw.tileGrid(QuasarTiles.RED_LIGHT, 0, 24)
        draw.tileGrid(QuasarTiles.RED_LIGHT, 24, 24)
        draw.tileGrid(QuasarTiles.RED_LIGHT, 24, 0)
    }
}