import java.io.*

class BinaryFile {

    class Out(private val out: DataOutputStream) {
        private var isFinished = false

        @Throws(IOException::class)
        fun writeString(id: Int, data: String?) {
            out.writeInt(id)
            out.write(TYPE_STRING)
            out.writeUTF(data)
        }

        @Throws(IOException::class)
        fun writeInt(id: Int, data: Int) {
            out.writeInt(id)
            out.write(TYPE_INT)
            out.writeInt(data)
        }

        @Throws(IOException::class)
        fun writeDouble(id: Int, data: Double) {
            out.writeInt(id)
            out.write(TYPE_DOUBLE)
            out.writeDouble(data)
        }

        @Throws(IOException::class)
        fun writeBytes(id: Int, data: ByteArray) {
            out.writeInt(id)
            out.write(TYPE_BYTE_ARRAY)
            out.writeInt(data.size)
            out.write(data)
        }

        fun finish() {
            //Ensure we only call isFinished once, multiple times will be ignored
            if(!isFinished) {
                out.writeInt(-1)
                out.close()
                isFinished = true
            }
        }
    }

    class In(private val inp: DataInputStream) {

        fun read(record: BinaryOutput): Boolean {
            record.id = inp.readInt()

            if(record.id == -1) {
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
        const val TYPE_BINARY_RECORDS: Int = 5 //TODO: Support child binary record types

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