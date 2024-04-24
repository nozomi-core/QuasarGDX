package app.quasar.gdx.tiles

import app.quasar.qgl.tiles.GameTileset
import app.quasar.qgl.tiles.TilesetBuilder

class CoreTileset: GameTileset {

    override fun onCreateTiles(builder: TilesetBuilder) {
        builder.add(CoreTiles.TRANSPARENT,     0, 0)
        builder.add(CoreTiles.RED_LIGHT,      0, 1)
        builder.add(CoreTiles.RED_DARK,       0, 2)
        builder.add(CoreTiles.GREEN_LIGHT,    1, 1)
        builder.add(CoreTiles.SMILE,          0, 3)
    }
}