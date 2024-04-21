package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.DrawableApi
import app.quasar.qgl.tiles.GameOverlay

class EditOverlay: GameOverlay {

    override fun onCreate() {

    }

    override fun onDraw(draw: DrawableApi) {
        draw.tileGrid(CoreTiles.GREEN_LIGHT, 0, 0)
        draw.tileGrid(CoreTiles.RED_LIGHT, 0, 24)
        draw.tileGrid(CoreTiles.RED_LIGHT, 24, 24)
        draw.tileGrid(CoreTiles.RED_LIGHT, 24, 0)
    }
}