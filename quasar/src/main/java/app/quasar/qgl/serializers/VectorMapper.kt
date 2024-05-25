package app.quasar.qgl.serializers

import app.quasar.qgl.serialize.BinaryReader
import app.quasar.qgl.serialize.BinaryWriter
import app.quasar.qgl.serialize.QGLMapper
import com.badlogic.gdx.math.Vector3

class VectorMapper: QGLMapper<Vector3> {

    override fun toBinary(value: Vector3, out: BinaryWriter) {
        out.write(ID_X, value.x)
        out.write(ID_Y, value.y)
        out.write(ID_Z, value.z)
    }

    override fun toObject(inp: BinaryReader): Vector3 {
        return Vector3(
            inp.read(ID_X),
            inp.read(ID_Y),
            inp.read(ID_Z)
        )
    }

    companion object {
        const val ID_X = 0
        const val ID_Y = 1
        const val ID_Z = 2
    }
}