package app.quasar.gdx.tools.enginetest.mapper

import app.quasar.gdx.tools.enginetest.data.MissileData
import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.BinaryRecord
import app.quasar.qgl.serialize.QGLMapper
import com.badlogic.gdx.math.Vector3

object MissileMapper: QGLMapper<MissileData> {
    private const val ID_X = 1
    private const val ID_Y = 2
    private const val ID_Z = 3

    override fun toBinary(data: MissileData): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_X, data.position.x),
            BinaryRecord(ID_Y, data.position.y),
            BinaryRecord(ID_Z, data.position.z)

        )
    }

    override fun toEntity(bin: BinaryObject): MissileData {
        return MissileData(
            position = Vector3(
                bin.value(ID_X),
                bin.value(ID_Y),
                bin.value(ID_Z)
            )
        )
    }
}