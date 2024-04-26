package app.quasar.gdx.tools.game.data

import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.BinaryRecord
import app.quasar.qgl.serialize.QGLMapper
import com.badlogic.gdx.math.Vector3

data class PlayerData(
    var position: Vector3,
    var rotate: Float
)

class PlayerMapper: QGLMapper<PlayerData> {
    override fun toBinary(data: PlayerData): Array<BinaryRecord> {
        TODO("Not yet implemented")
    }

    override fun toEntity(bin: BinaryObject): PlayerData {
        TODO("Not yet implemented")
    }
}