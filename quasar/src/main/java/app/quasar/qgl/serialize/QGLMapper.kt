package app.quasar.qgl.serialize

interface QGLMapper<T> {
    fun toBinary(value: T): Array<BinaryRecord>
    fun toObject(inp: BinaryObject): T
}