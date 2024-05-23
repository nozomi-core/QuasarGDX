package app.quasar.gdx.tools.enginetest.data

import app.quasar.gdx.tools.model.TileInfo
import app.quasar.qgl.engine.core.GameData
import app.quasar.qgl.serialize.BinProp

class TilemapData: GameData {
   @BinProp(0)          var tiles: MutableList<TileInfo> = mutableListOf()
}