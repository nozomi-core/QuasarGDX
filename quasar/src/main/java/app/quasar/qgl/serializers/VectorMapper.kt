package app.quasar.qgl.serializers

import app.quasar.qgl.serialize.*
import com.badlogic.gdx.math.Vector3

object VectorMapper: QGLMapper<Vector3> {
    const val ID_X = 0
    const val ID_Y = 1
    const val ID_Z = 2

    override fun toBinary(value: Vector3): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_X, value.x),
            BinaryRecord(ID_Y, value.y),
            BinaryRecord(ID_Z, value.z)
        )
    }

    override fun toObject(inp: BinaryObject): Vector3 {
        return Vector3(
            inp.value(ID_X),
            inp.value(ID_Y),
            inp.value(ID_Z)
        )
    }
}