import app.quasar.gdx.tools.BinaryUtils
import java.io.*

class BinaryFile {

    class Out(private val out: DataOutputStream) {
        private var isFinished = false

        @Throws(IOException::class)
        fun writeString(id: Int, data: String) {
            validateId(id)
            out.writeInt(id)
            val bytes = BinaryUtils.xor255(data.toByteArray(Charsets.UTF_8))
            out.write(TYPE_STRING)
            out.writeInt(bytes.size)
            out.write(bytes)
        }

        @Throws(IOException::class)
        fun writeStringArray(id: Int, data: Array<String>) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_STRING_ARRAY)
            out.writeInt(data.size)
            data.forEach { iString ->
                val bytes = BinaryUtils.xor255(iString.toByteArray(Charsets.UTF_8))
                out.writeInt(bytes.size)
                out.write(bytes)
            }
        }

        @Throws(IOException::class)
        fun writeInt(id: Int, data: Int) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_INT)
            out.writeInt(data)
        }

        @Throws(IOException::class)
        fun writeIntArray(id: Int, data: Array<Int>) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_INT_ARRAY)
            out.writeInt(data.size)
            data.forEach {
                out.writeInt(it)
            }
        }

        @Throws(IOException::class)
        fun writeDoubleArray(id: Int, data: Array<Double>) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_DOUBLE_ARRAY)
            out.writeInt(data.size)
            data.forEach {
                out.writeDouble(it)
            }
        }

        @Throws(IOException::class)
        fun writeBytes(id: Int, data: ByteArray) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_BYTE_ARRAY)
            out.writeInt(data.size)
            out.write(data)
        }

        @Throws(IOException::class)
        fun writeObject(id: Int, data: BinaryObject) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_BINARY_OBJECT)
            out.writeInt(data.records.size)
            data.records.forEach {
                writeRecord(it)
            }
        }

        @Throws(IOException::class)
        fun writeObjectArray(id: Int, data: List<BinaryObject>) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_BINARY_OBJECT_ARRAY)
            out.writeInt(data.size)
            data.forEach { item ->
                writeObject(item.id, item)
            }
        }

        private fun writeRecord(record: BinaryRecord) {
            when(record.data) {
                is String -> writeString(record.id, record.data)
                else -> throw Exception("Not implemented")
            }
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
                TYPE_INT_ARRAY -> {
                    val size = inp.readInt()
                    val intArray = Array(size) { 0 }
                    intArray.forEachIndexed { index, _ ->
                        intArray[index] = inp.readInt()
                    }
                    record.data = intArray
                }
                TYPE_DOUBLE -> {
                    record.data = inp.readDouble()
                }
                TYPE_DOUBLE -> {
                    val size = inp.readInt()
                    val doubleArray = Array(size){ 0.0 }
                    doubleArray.forEachIndexed { index, _ ->
                        doubleArray[index] = inp.readDouble()
                    }
                    record.data = doubleArray
                }
                TYPE_BYTE_ARRAY -> {
                    val size = inp.readInt()
                    val byteData = ByteArray(size)
                    record.data = inp.read(byteData)
                }
                TYPE_STRING -> {
                    //Decode the XOR operation to get the original string in plain text
                    val size = inp.readInt()
                    val byteData = ByteArray(size)
                    inp.read(byteData)
                    record.data = String(BinaryUtils.xor255(byteData))
                }
                TYPE_STRING -> {
                    val size = inp.readInt()
                    val stringArray = Array(size){ "" }
                    stringArray.forEachIndexed { index, _ ->
                        val size = inp.readInt()
                        val byteStrings = ByteArray(size)
                        inp.read(byteStrings)
                        val decodedBytes = BinaryUtils.xor255(byteStrings)
                        stringArray[index] = String(decodedBytes)
                    }
                    record.data = stringArray
                }
                TYPE_BINARY_OBJECT -> {
                    val size = inp.readInt()
                    val objectList = mutableListOf<BinaryRecord>()
                    //Call the read on itself to read the complex object
                    val output = BinaryOutput()

                    for(i in 0 until size) {
                        read(output)
                        objectList.add(output.toBinaryRecord())
                    }

                    record.data = BinaryObject(record.id, objectList.toList())
                }
                TYPE_BINARY_OBJECT_ARRAY -> {
                    val size = inp.readInt()
                    val objectList = mutableListOf<BinaryObject>()

                    val output = BinaryOutput()

                    for(i in 0 until size) {
                        read(output)
                        objectList.add(output.data as BinaryObject)
                    }
                    record.data = objectList
                }


                else -> throw Exception("type id not supported")
            }

            return true
        }
    }

    companion object {
        const val TYPE_INT: Int =                   1
        const val TYPE_INT_ARRAY: Int =             2
        const val TYPE_STRING: Int =                3
        const val TYPE_STRING_ARRAY: Int =          4
        const val TYPE_DOUBLE: Int =                5
        const val TYPE_DOUBLE_ARRAY: Int =          6
        const val TYPE_BYTE_ARRAY: Int =            7
        const val TYPE_BINARY_OBJECT: Int =         8
        const val TYPE_BINARY_OBJECT_ARRAY: Int =   9

        const val ID_END_OF_DATA = -1

        fun createFileOut(file: File, writeCallback: (out: Out) -> Unit) {
            val stream = Out(DataOutputStream(FileOutputStream(file)))
            writeCallback(stream)
            stream.finish()
        }

        fun createFileIn(file: File): In = In(DataInputStream(FileInputStream(file)))

        fun createMemoryOut(writeCallback: (out: Out) -> Unit): InlineBinaryFormat {
            val byteOut = ByteArrayOutputStream()
            val memoryStream = Out(DataOutputStream(byteOut))
            writeCallback(memoryStream)
            memoryStream.finish()
            return InlineBinaryFormat(byteOut.toByteArray())
        }

        fun createMemoryIn(memory: InlineBinaryFormat): In {
            return In(DataInputStream(ByteArrayInputStream(memory.byteData)))
        }
    }
}

class BinaryOutput {
    var id = -1
    var type = -1
    var data: Any = -1

    fun toBinaryRecord() = BinaryRecord(id, data)

}

class InlineBinaryFormat(val byteData: ByteArray)

class BinaryObject(
    val id: Int,
    val records: List<BinaryRecord>
)

class BinaryRecord(
    val id: Int,
    val data: Any
)

