package app.quasar.qgl.serialize

import java.io.*

class QGLBinary {

    class Out(private val out: DataOutputStream) {
        private var isFinished = false

        @Throws(IOException::class)
        fun writeInt(id: Int, data: Int) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_INT)
            out.writeInt(data)
        }

        @Throws(IOException::class)
        fun writeIntArray(id: Int, data: IntArray) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_INT_ARRAY)
            out.writeInt(data.size)
            data.forEach {
                out.writeInt(it)
            }
        }

        @Throws(IOException::class)
        fun writeLong(id: Int, data: Long) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_LONG)
            out.writeLong(data)
        }

        @Throws(IOException::class)
        fun writeLongArray(id: Int, data: LongArray) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_LONG_ARRAY)
            out.writeInt(data.size)
            data.forEach {
                out.writeLong(it)
            }
        }

        @Throws(IOException::class)
        fun writeDouble(id: Int, data: Double) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_DOUBLE)
            out.writeDouble(data)
        }

        @Throws(IOException::class)
        fun writeDoubleArray(id: Int, data: DoubleArray) {
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
            out.write(TYPE_BYTES)
            out.writeInt(data.size)
            out.write(data)
        }

        @Throws(IOException::class)
        fun writeByteMatrix(id: Int, data: ByteMatrix) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_BYTE_MATRIX)
            out.writeInt(data.size)
            for(index in 0 until data.size) {
                val childBytes = data[index]
                out.writeInt(childBytes.size)
                out.write(childBytes)
            }
        }

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
        fun writeStringMatrix(id: Int, data: StringMatrix) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_STRING_MATRIX)
            out.writeInt(data.size)

            for(index in 0 until data.size) {
                val bytes = BinaryUtils.xor255(data[index].toByteArray(Charsets.UTF_8))
                out.writeInt(bytes.size)
                out.write(bytes)
            }
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
                is Int -> writeInt(record.id, record.data)
                is IntArray -> writeIntArray(record.id, record.data)
                is Long -> writeLong(record.id, record.data)
                is LongArray -> writeLongArray(record.id, record.data)
                is Double -> writeDouble(record.id, record.data)
                is DoubleArray -> writeDoubleArray(record.id, record.data)
                is ByteArray -> writeBytes(record.id, record.data)
                is ByteMatrix -> writeByteMatrix(record.id, record.data)
                is String -> writeString(record.id, record.data)


                else -> throw Exception("Type in record not supported")
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
                    val typeSize = inp.readInt()
                    val intArray = IntArray(typeSize)
                    intArray.forEachIndexed { index, _ ->
                        intArray[index] = inp.readInt()
                    }
                    record.data = intArray
                }
                TYPE_LONG -> {
                    record.data = inp.readLong()
                }
                TYPE_LONG_ARRAY -> {
                    val typeSize = inp.readInt()
                    val longArray = LongArray(typeSize)
                    longArray.forEachIndexed { index, _ ->
                        longArray[index] = inp.readLong()
                    }
                    record.data = longArray
                }
                TYPE_DOUBLE -> {
                    record.data = inp.readDouble()
                }
                TYPE_DOUBLE_ARRAY -> {
                    val typeSize = inp.readInt()
                    val doubleArray = DoubleArray(typeSize)
                    doubleArray.forEachIndexed { index, _ ->
                        doubleArray[index] = inp.readDouble()
                    }
                    record.data = doubleArray
                }
                TYPE_BYTES -> {
                    val typeSize = inp.readInt()
                    val byteData = ByteArray(typeSize)
                    record.data = inp.read(byteData)
                }
                TYPE_BYTE_MATRIX -> {
                    val size = inp.readInt()
                    val bytesArray = mutableListOf<ByteArray>()
                    for(i in 0 until size) {
                        val byteArraySize = inp.readInt()
                        val childBytes = ByteArray(byteArraySize)
                        inp.read(childBytes)
                        bytesArray.add(childBytes)
                    }
                    record.data = ByteMatrix(bytesArray)
                }
                TYPE_STRING -> {
                    //Decode the XOR operation to get the original string in plain text
                    val typeSize = inp.readInt()
                    val byteData = ByteArray(typeSize)
                    inp.read(byteData)
                    record.data = String(BinaryUtils.xor255(byteData))
                }
                TYPE_STRING_MATRIX -> {
                    val typeSize = inp.readInt()
                    val stringList = mutableListOf<String>()

                    for(index in 0 until typeSize) {
                        val childSize = inp.readInt()
                        val byteStrings = ByteArray(childSize)
                        inp.read(byteStrings)
                        val decodedBytes = BinaryUtils.xor255(byteStrings)
                        stringList.add(String(decodedBytes))
                    }

                    record.data = StringMatrix(stringList)
                }
                TYPE_BINARY_OBJECT -> {
                    val typeSize = inp.readInt()
                    val objectList = mutableListOf<BinaryRecord>()
                    //Call the read on itself to read the complex object
                    val output = BinaryOutput()

                    for(i in 0 until typeSize) {
                        read(output)
                        objectList.add(output.toBinaryRecord())
                    }

                    record.data = BinaryObject(record.id, objectList.toList())
                }
                TYPE_BINARY_OBJECT_ARRAY -> {
                    val typeSize = inp.readInt()
                    val objectList = mutableListOf<BinaryObject>()

                    val output = BinaryOutput()

                    for(i in 0 until typeSize) {
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
        const val TYPE_LONG: Int =                  3
        const val TYPE_LONG_ARRAY: Int =            4
        const val TYPE_DOUBLE: Int =                5
        const val TYPE_DOUBLE_ARRAY: Int =          6
        const val TYPE_BYTES: Int =                 7
        const val TYPE_BYTE_MATRIX: Int =           8
        const val TYPE_STRING: Int =                9
        const val TYPE_STRING_MATRIX: Int =         10
        const val TYPE_BINARY_OBJECT: Int =         11
        const val TYPE_BINARY_OBJECT_ARRAY: Int =   12

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

class ByteMatrix(private val matrix: List<ByteArray>) {
    val size: Int get() = matrix.size
    operator fun get(index: Int): ByteArray {
        return matrix[index]
    }
}

class StringMatrix(private val matrix: List<String>) {
    val size: Int get() = matrix.size
    operator fun get(index: Int): String {
        return matrix[index]
    }
}

