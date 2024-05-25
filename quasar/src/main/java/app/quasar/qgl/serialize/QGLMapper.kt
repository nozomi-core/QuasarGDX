package app.quasar.qgl.serialize

interface BinaryWriter {
    fun write(id: Int,  value: Any)
}

interface BinaryReader {
    fun <T> read(id: Int): T
}

interface QGLMapper<T> {
    fun toBinary(value: T, out: BinaryWriter)
    fun toObject(inp: BinaryReader): T
}