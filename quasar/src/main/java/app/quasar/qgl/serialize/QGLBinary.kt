package app.quasar.qgl.serialize

import java.io.*

interface DataWriter {
    fun writeInt(value: Int)
    fun write(value: Int)
    fun writeLong(value: Long)
    fun writeFloat(value: Float)
    fun writeDouble(value: Double)
    fun writeBoolean(value: Boolean)
    fun write(value: ByteArray)
    fun writeString(value: String)
    fun close()
}

class QGLBinary {

    inner class Out(private val out: DataWriter) {
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
        fun writeFloat(id: Int, data: Float) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_FLOAT)
            out.writeFloat(data)
        }

        @Throws(IOException::class)
        fun writeFloatArray(id: Int, data: FloatArray) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_FLOAT_ARRAY)
            out.writeInt(data.size)
            data.forEach {
                out.writeFloat(it)
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
        fun writeBoolean(id: Int, data: Boolean) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_BOOLEAN)
            out.writeBoolean(data)
        }

        @Throws(IOException::class)
        fun writeBooleanArray(id: Int, data: BooleanArray) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_BOOLEAN_ARRAY)
            out.writeInt(data.size)
            data.forEach {
                out.writeBoolean(it)
            }
        }

        @Throws(IOException::class)
        fun writeChar(id: Int, data: Char) {
            writeStringType(id = id, type = TYPE_CHAR, data = "$data")
        }

        @Throws(IOException::class)
        fun writeCharArray(id: Int, data: CharArray) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_CHAR_ARRAY)
            out.writeInt(data.size)
            data.forEach {
                writeStringType(0, TYPE_CHAR, "$it")
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
        fun writeByteMatrix(id: Int, data: ByteCodex) {
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
        private fun writeStringType(id: Int, type: Int, data: String) {
            validateId(id)
            out.writeInt(id)
            val bytes = BinaryUtils.xor255(data.toByteArray(Charsets.UTF_8))
            out.write(type)
            out.writeInt(bytes.size)
            out.write(bytes)
        }

        @Throws(IOException::class)
        fun writeString(id: Int, data: String) {
            writeStringType(id = id, type = TYPE_STRING, data = data)
        }

        @Throws(IOException::class)
        fun writeStringMatrix(id: Int, data: StringCodex) {
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
            out.writeString(data.classId)
            out.writeInt(data.size)
            for(index in 0 until data.size) {
                writeRecord(data[index])
            }
        }

        @Throws(IOException::class)
        private fun writeFrameStart(id: Int) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_FRAME_START)
        }

        @Throws(IOException::class)
        private fun writeFrameEnd(id: Int) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_FRAME_END)
        }

        @Throws(IOException::class)
        fun writeFrame(id: Int, callback: () -> Unit) {
            validateId(id)
            writeFrameStart(id)
            callback()
            writeFrameEnd(id)
        }

        @Throws(IOException::class)
        private fun writeObjectBlob(id: Int, value: BinaryObject) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_OBJECT_BLOB)
            out.writeString(value.classId)
            out.writeInt(value.size)

            for(i in 0 until value.size) {
                writeRecord(value[i])
            }
        }

        @Throws(IOException::class)
        private fun writeIntMatrix(id: Int, value: IntMatrix) {
            validateId(id)
            out.writeInt(id)
            out.write(TYPE_INT_MATRIX)
            //Column size
            val columns = value.matrix.size
            val rows = value.matrix[0].size

            out.writeInt(columns)
            out.writeInt(rows)

            for(x in 0 until columns) {
                val nextColumn = value.matrix[x]

                for(y in 0 until rows) {
                    val matrixValue = nextColumn[y]
                    out.writeInt(matrixValue)
                }
            }
        }

        private fun writeRecord(record: BinaryRecord) {
            when(record.data) {
                is Int -> writeInt(record.id, record.data)
                is IntArray -> writeIntArray(record.id, record.data)
                is Long -> writeLong(record.id, record.data)
                is LongArray -> writeLongArray(record.id, record.data)
                is Float -> writeFloat(record.id, record.data)
                is FloatArray -> writeFloatArray(record.id, record.data)
                is Double -> writeDouble(record.id, record.data)
                is DoubleArray -> writeDoubleArray(record.id, record.data)
                is Boolean -> writeBoolean(record.id, record.data)
                is BooleanArray -> writeBooleanArray(record.id, record.data)
                is Char -> writeChar(record.id, record.data)
                is CharArray -> writeCharArray(record.id, record.data)
                is ByteArray -> writeBytes(record.id, record.data)
                is ByteCodex -> writeByteMatrix(record.id, record.data)
                is String -> writeString(record.id, record.data)
                is StringCodex -> writeStringMatrix(record.id, record.data)
                is BinaryObject -> writeObjectBlob(record.id, record.data)
                is IntMatrix -> writeIntMatrix(record.id, record.data)
                is BinaryObjectCodex -> throw Exception("for now we don't support records having objects, the idea is flat structure")

                else -> throw Exception("Type in record not supported")
            }
        }

        private fun validateId(id: Int) {
            if(id <= -1) {
                throw Exception("Byte id cannot be negative, they are reserved ids")
            }
        }

        fun close() {
            //Ensure we only call isFinished once, multiple times will be ignored
            if(!isFinished) {
                out.writeInt(ID_END_OF_DATA)
                out.close()
                isFinished = true
            }
        }
    }

    inner class In(private val inp: DataInputStream) {

        fun readFrame(callback: (BinaryOutput) -> Unit) {
            val output = BinaryOutput()
            read(output)
            if(output.type != TYPE_FRAME_START) throw Exception("Current output is not a frame")
            read(output)

            while(output.type != TYPE_FRAME_END) {
                callback(output)
                read(output)
            }
        }

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
                TYPE_FLOAT -> {
                    record.data = inp.readFloat()
                }
                TYPE_FLOAT_ARRAY -> {
                    val typeSize = inp.readInt()
                    val floatArray = FloatArray(typeSize)
                    floatArray.forEachIndexed { index, _ ->
                        floatArray[index] = inp.readFloat()
                    }
                    record.data = floatArray
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
                TYPE_BOOLEAN -> {
                    record.data = inp.readBoolean()
                }
                TYPE_BOOLEAN_ARRAY -> {
                    val typeSize = inp.readInt()
                    val doubleArray = BooleanArray(typeSize)
                    doubleArray.forEachIndexed { index, _ ->
                        doubleArray[index] = inp.readBoolean()
                    }
                    record.data = doubleArray
                }
                TYPE_CHAR -> {
                    val charString = readStringType()
                    record.data = charString[0]
                }
                TYPE_CHAR_ARRAY -> {
                    val typeSize = inp.readInt()
                    val charArray = CharArray(typeSize)
                    charArray.forEachIndexed { index, _ ->
                        val str = readStringType()
                        charArray[index] = str[0]
                    }
                    record.data = charArray
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
                    record.data = ByteCodex(bytesArray)
                }
                TYPE_STRING -> {
                    record.data = readStringType()
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

                    record.data = StringCodex(stringList)
                }
                TYPE_BINARY_OBJECT -> {
                    val classId = inp.readUTF()

                    val typeSize = inp.readInt()
                    val objectList = mutableListOf<BinaryRecord>()
                    //Call the read on itself to read the complex object
                    val output = BinaryOutput()

                    for(i in 0 until typeSize) {
                        read(output)
                        objectList.add(output.toBinaryRecord())
                    }

                    record.data = BinaryObject(classId, objectList.toTypedArray())
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
                TYPE_FRAME_START -> {
                    record.data = Unit
                }
                TYPE_FRAME_END -> {
                    record.data = Unit
                }
                TYPE_OBJECT_BLOB -> {
                    val recordList = mutableListOf<BinaryRecord>()

                    val output = BinaryOutput()

                    val classId = inp.readUTF()
                    val typeSize = inp.readInt()

                    for(i in 0 until typeSize) {
                        read(output)
                        recordList.add(
                            BinaryRecord(output.id, output.data)
                        )
                    }
                    record.data = BinaryObject(classId, recordList.toTypedArray())
                }
                TYPE_INT_MATRIX -> {
                    val columns = inp.readInt()
                    val rows = inp.readInt()

                    val matrix = IntMatrix(rows, columns)

                    for(x in 0 until columns) {
                        for(y in 0 until rows) {
                            val nextValue = inp.readInt()
                            matrix.set(x, y, nextValue)
                        }
                    }
                    record.data = matrix
                }

                else -> throw Exception("type id not supported")
            }

            return true
        }

        private fun readStringType(): String {
            val typeSize = inp.readInt()
            val byteData = ByteArray(typeSize)
            inp.read(byteData)
            return String(BinaryUtils.xor255(byteData))
        }
    }

    companion object {
        const val TYPE_INT: Int =                   1
        const val TYPE_INT_ARRAY: Int =             2
        const val TYPE_LONG: Int =                  3
        const val TYPE_LONG_ARRAY: Int =            4
        const val TYPE_FLOAT: Int =                 5
        const val TYPE_FLOAT_ARRAY =                6
        const val TYPE_DOUBLE: Int =                7
        const val TYPE_DOUBLE_ARRAY: Int =          8
        const val TYPE_BOOLEAN: Int =               9
        const val TYPE_BOOLEAN_ARRAY: Int =         10
        const val TYPE_CHAR: Int =                  11
        const val TYPE_CHAR_ARRAY: Int =            12
        const val TYPE_BYTES: Int =                 13
        const val TYPE_BYTE_MATRIX: Int =           14
        const val TYPE_STRING: Int =                15
        const val TYPE_STRING_MATRIX: Int =         16
        const val TYPE_BINARY_OBJECT: Int =         17
        const val TYPE_BINARY_OBJECT_ARRAY: Int =   18
        const val TYPE_FRAME_START: Int =           19
        const val TYPE_FRAME_END: Int =             20
        const val TYPE_OBJECT_BLOB: Int =           21
        const val TYPE_INT_MATRIX: Int =            22

        const val ID_NO_DATA_READ = -50
        const val ID_END_OF_DATA = -100

        fun createFileOut(file: File, writeCallback: (out: Out) -> Unit) {
            val binary = QGLBinary()
            val stream = binary.Out(BinaryDataWriter(FileOutputStream(file)))
            writeCallback(stream)
            stream.close()
        }

        fun createFileIn(file: File): In = QGLBinary().In(DataInputStream(FileInputStream(file)))

        fun createMemoryOut(writeCallback: (out: Out) -> Unit): InlineBinaryFormat {
            val byteOut = ByteArrayOutputStream()
            val memoryStream = QGLBinary().Out(BinaryDataWriter(byteOut))
            writeCallback(memoryStream)
            memoryStream.close()
            return InlineBinaryFormat(byteOut.toByteArray())
        }

        fun createMemoryIn(memory: InlineBinaryFormat): In {
            return QGLBinary().In(DataInputStream(ByteArrayInputStream(memory.byteData)))
        }
    }
}

class BinaryOutput {
    var id = QGLBinary.ID_NO_DATA_READ
    var type = -1
    var data: Any = -1

    fun hasData() = id != QGLBinary.ID_END_OF_DATA
    fun toBinaryRecord() = BinaryRecord(id, data)
}

class InlineBinaryFormat(val byteData: ByteArray)

class BinaryObject(
    val classId: String,
    private val matrix: Array<BinaryRecord>
): FastIterator<BinaryRecord> {
    override val size: Int get() = matrix.size
    override operator fun get(index: Int): BinaryRecord {
        return matrix[index]
    }
    fun findId(id: Int): BinaryRecord? {
        for(index in 0 until size) {
            if(matrix[index].id == id) {
                return matrix[index]
            }
        }
        return null
    }
    fun <T> value(id: Int): T {
        return findId(id)!!.data as T
    }
}

class ByteCodex(private val list: List<ByteArray>) {
    val size: Int get() = list.size
    operator fun get(index: Int): ByteArray {
        return list[index]
    }
}

class StringCodex(private val list: List<String>) {
    val size: Int get() = list.size
    operator fun get(index: Int): String {
        return list[index]
    }
}

class BinaryObjectCodex(private val list: List<BinaryObject>) {
    val size: Int get() = list.size
    operator fun get(index: Int): BinaryObject {
        return list[index]
    }
}

class IntMatrix(row: Int, column: Int) {
    internal val matrix = Array(column) { IntArray(row) { 0 } }

    fun set(x: Int, y: Int, value: Int) {
        matrix[x][y] = value
    }
    fun get(x: Int, y: Int): Int = matrix[x][y]
}

interface FastIterator<T> {
    val size: Int
    operator fun get(index: Int): T

    fun forEach(callback: (item: T) -> Unit) {
        for(index in 0 until size) {
            callback(this[index])
        }
    }
}



