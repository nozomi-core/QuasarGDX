package app.quasar.qgl.serialize

class StringDataWriter: DataWriter {
    private val builder = StringBuilder()

    override fun writeInt(value: Int) {
        builder.append("int:${value};")
    }

    override fun write(value: Int) {
        builder.append("byte:${value};")
    }

    override fun write(value: ByteArray) {
        builder.append("bytes:[")
        value.forEach {
            builder.append("$it,")
        }
        builder.append("];")
    }

    override fun writeLong(value: Long) {
        builder.append("long:${value};")
    }

    override fun writeFloat(value: Float) {
        builder.append("float:${value};")
    }

    override fun writeDouble(value: Double) {
        builder.append("double:${value};")
    }

    override fun writeBoolean(value: Boolean) {
        builder.append("boolean:${value};")
    }

    override fun close() {}

    override fun toString(): String {
        return builder.toString()
    }
}