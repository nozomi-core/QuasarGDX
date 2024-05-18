package app.quasar.gdx.tools.enginetest.mapper

import app.quasar.gdx.tools.enginetest.data.DestroyData
import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.BinaryRecord
import app.quasar.qgl.serialize.QGLMapper

object DestroyMapper: QGLMapper<DestroyData> {
    private const val ID_TIME = 0

    override fun toBinary(data: DestroyData): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_TIME, data.totalTime)
        )
    }

    override fun toEntity(bin: BinaryObject): DestroyData {
        return DestroyData(
            totalTime = bin.value(ID_TIME)
        )
    }
}