import app.quasar.gdx.tools.BinaryUtils
import java.io.*
import java.nio.charset.Charset
import kotlin.experimental.xor

class BinaryFile {

    class Out(private val out: DataOutputStream) {
        private var isFinished = false

        @Throws(IOException::class)
        fun writeString(id: Int, data: String) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_STRING)
            out.writeUTF(data)
        }

        @Throws(IOException::class)
        fun writeStringXOR(id: Int, data: String) {
            validateId(id)
            out.writeInt(id)
            val bytes = BinaryUtils.xor255(data.toByteArray(Charsets.UTF_8))
            out.write(TYPE_STRING_255_XOR)
            out.writeInt(bytes.size)
            out.write(bytes)
        }

        @Throws(IOException::class)
        fun writeInt(id: Int, data: Int) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_INT)
            out.writeInt(data)
        }

        @Throws(IOException::class)
        fun writeDouble(id: Int, data: Double) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_DOUBLE)
            out.writeDouble(data)
        }

        @Throws(IOException::class)
        fun writeBytes(id: Int, data: ByteArray) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_BYTE_ARRAY)
            out.writeInt(data.size)
            out.write(data)
        }

        private fun validateId(id: Int) {
            if(id <= -1) {
                throw Exception("Byte id cannot be negative, they are reserved ids")
            }
        }

        fun finish() {
            //Ensure we only call isFinished once, multiple times will be ignored
            if(!isFinished) {
                out.writeInt(ID_END_OF_DATA)
                out.close()
                isFinished = true
            }
        }

    }

    class In(private val inp: DataInputStream) {

        fun read(record: BinaryOutput): Boolean {
            record.id = inp.readInt()

            if(record.id == ID_END_OF_DATA) {
                inp.close()
                return false
            }

            record.type = inp.read()

            when(record.type) {
                TYPE_INT -> {
                    record.data = inp.readInt()
                }
                TYPE_STRING -> {
                    record.data = inp.readUTF()
                }
                TYPE_DOUBLE -> {
                    record.data = inp.readDouble()
                }
                TYPE_BYTE_ARRAY -> {
                    val size = inp.readInt()
                    val byteData = ByteArray(size)
                    record.data = inp.read(byteData)
                }
                TYPE_STRING_255_XOR -> {
                    //Decode the XOR operation to get the original string in plain text
                    val size = inp.readInt()
                    val byteData = ByteArray(size)
                    inp.read(byteData)
                    record.data = String(BinaryUtils.xor255(byteData))
                }

                else -> throw Exception("type id not supported")
            }

            return true
        }
    }

    companion object {
        const val TYPE_INT: Int = 1
        const val TYPE_STRING: Int = 2
        const val TYPE_DOUBLE: Int = 3
        const val TYPE_BYTE_ARRAY: Int = 4
        const val TYPE_STRING_255_XOR = 5
        const val TYPE_BINARY_RECORDS: Int = 6 //TODO: Support child binary record types

        const val ID_END_OF_DATA = -1

        fun createFileOut(file: File, writeCallback: (out: Out) -> Unit) {
            val stream = Out(DataOutputStream(FileOutputStream(file)))
            writeCallback(stream)
            stream.finish()
        }

        fun createFileIn(file: File): In = In(DataInputStream(FileInputStream(file)))
    }
}

class BinaryOutput {
    var id = -1
    var type = -1
    var data: Any = -1
}