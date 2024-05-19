package app.quasar.gdx.tools.enginetest.mapper

import app.quasar.gdx.tools.enginetest.data.PlayerData
import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.BinaryRecord
import app.quasar.qgl.serialize.QGLMapper
import com.badlogic.gdx.math.Vector3

object PlayerMapper: QGLMapper<PlayerData> {
    private const val ID_X = 1
    private const val ID_Y = 2
    private const val ID_Z = 3
    private const val ID_ROTATION = 4

    override fun toBinary(data: PlayerData): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_X, data.position.x),
            BinaryRecord(ID_Y, data.position.y),
            BinaryRecord(ID_Z, data.position.z),
            BinaryRecord(ID_ROTATION, data.rotation)
        )
    }

    override fun toEntity(bin: BinaryObject): PlayerData {
        return PlayerData(
            position = Vector3(
                bin.value(ID_X),
                bin.value(ID_Y),
                bin.value(ID_Z),
            ),
            bin.value(ID_ROTATION)
        )
    }
}