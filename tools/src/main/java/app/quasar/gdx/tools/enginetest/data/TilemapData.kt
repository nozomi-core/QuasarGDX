package app.quasar.gdx.tools.enginetest.data

import app.quasar.gdx.tools.model.TileInfo
import app.quasar.gdx.tools.model.createRandomTileInfo
import app.quasar.qgl.serialize.BinProp
import app.quasar.qgl.serialize.IntMatrix
import app.quasar.qgl.serialize.QGLEntity

@QGLEntity("tilemap_data")
class TilemapData {
       @BinProp(0)   var tiles = IntMatrix(100, 100)
}