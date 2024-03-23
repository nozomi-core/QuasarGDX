package app.quasar.gdx.tiles

import app.quasar.qgl.tiles.GameTileset
import app.quasar.qgl.tiles.TilesetBuilder

class QuasarTileset: GameTileset {

    override fun onCreateTiles(builder: TilesetBuilder) {
        builder.add(QuasarTiles.TRANSPARENT,     0, 0)
        builder.add(QuasarTiles.RED_LIGHT,      0, 1)
        builder.add(QuasarTiles.RED_DARK,       0, 2)
        builder.add(QuasarTiles.GREEN_LIGHT,    1, 1)
    }
}