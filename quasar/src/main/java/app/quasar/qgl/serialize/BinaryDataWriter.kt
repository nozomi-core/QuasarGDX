package app.quasar.qgl.serialize

import java.io.DataOutputStream
import java.io.OutputStream

class BinaryDataWriter(outputStream: OutputStream): DataWriter {
    private val out = DataOutputStream(outputStream)

    override fun writeInt(value: Int) {
        out.writeInt(value)
    }

    override fun write(value: Int) {
        out.write(value)
    }

    override fun write(value: ByteArray) {
        out.write(value)
    }

    override fun writeLong(value: Long) {
        out.writeLong(value)
    }

    override fun writeFloat(value: Float) {
        out.writeFloat(value)
    }

    override fun writeDouble(value: Double) {
        out.writeDouble(value)
    }

    override fun writeBoolean(value: Boolean) {
        out.writeBoolean(value)
    }

    override fun writeString(value: String) {
        out.writeUTF(value)
    }

    override fun close() {
        out.close()
    }
}