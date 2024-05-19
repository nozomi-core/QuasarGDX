package app.quasar.qgl.serialize

interface QGLMapper<T> {
    fun toBinary(data: T): Array<BinaryRecord>
    fun toEntity(bin: BinaryObject): T
}