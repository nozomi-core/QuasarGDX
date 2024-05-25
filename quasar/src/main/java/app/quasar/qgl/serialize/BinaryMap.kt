package app.quasar.qgl.serialize

import com.badlogic.gdx.math.Vector3

class BinaryMap {

    fun toBinary(value: Any): Any {
        return if(value is Vector3) {
            BinaryObject(3,
                arrayOf(
                    BinaryRecord(1, value.x),
                    BinaryRecord(2, value.y),
                    BinaryRecord(3, value.z)
                )
            )
        } else {
            value
        }
    }

    fun toObject(bin: BinaryObject): Any {
        return Vector3(
            bin.value(1),
            bin.value(2),
            bin.value(3)
        )
    }
}