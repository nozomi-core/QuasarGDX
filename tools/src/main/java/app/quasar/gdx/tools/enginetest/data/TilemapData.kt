package app.quasar.gdx.tools.enginetest.data

import app.quasar.gdx.tools.model.TileInfo
import app.quasar.qgl.engine.core.GameData
import app.quasar.qgl.serialize.BinProp
import app.quasar.qgl.serialize.QGLEntity

@QGLEntity("tilemap_data")
class TilemapData {
       var tiles: MutableList<TileInfo> = mutableListOf()
}