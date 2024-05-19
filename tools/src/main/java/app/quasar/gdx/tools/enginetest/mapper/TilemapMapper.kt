package app.quasar.gdx.tools.enginetest.mapper

import app.quasar.gdx.tools.enginetest.data.TilemapData
import app.quasar.gdx.tools.model.createRandomTileInfo
import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.BinaryRecord
import app.quasar.qgl.serialize.QGLMapper

object TilemapMapper: QGLMapper<TilemapData> {

    override fun toBinary(data: TilemapData): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(9, "example")
        )
    }

    override fun toEntity(bin: BinaryObject): TilemapData {
        return TilemapData(
            tiles = createRandomTileInfo(100, 100).toMutableList()
        )
    }
}