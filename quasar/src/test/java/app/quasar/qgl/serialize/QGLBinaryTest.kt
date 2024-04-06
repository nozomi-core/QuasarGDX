package app.quasar.qgl.serialize

import org.junit.Assert.*
import org.junit.Test

/**
 * PLEASE READ!!
* When asserting the [BinaryOutput.type] use Hard value inline and not the [QGLBinary.TYPE_INT]
 *
 */
class QGLBinaryTest {

    private val DOUBLE_DELTA = 1e-15

    //WARNING! Do not change these ever (You may add to the list), these ensure you don't break the file type format
    private object TestBinaryType {
        const val INT = 1
        const val INT_ARRAY = 2
        const val LONG = 3
        const val LONG_ARRAY = 4
        const val DOUBLE = 5
        const val DOUBLE_ARRAY = 6
        const val BOOLEAN = 7
        const val BOOLEAN_ARRAY = 8
        const val STRING = 11
        const val STRING_MATRIX = 12
        const val OBJECT = 13
        const val OBJECT_MATRIX = 14
    }

    @Test
    fun testInt() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeInt(55, 12)
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)
        assertEquals(12, output.data)
        assertEquals(55, output.id)
        assertEquals(TestBinaryType.INT, output.type)
    }

    @Test
    fun testIntArray() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeIntArray(78, intArrayOf(1,8,45))
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)
        assertArrayEquals(intArrayOf(1, 8, 45), output.data as IntArray)
        assertEquals(78, output.id)
        assertEquals(TestBinaryType.INT_ARRAY, output.type)
    }

    @Test
    fun testLong() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeLong(938, 22L)
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)
        assertEquals(22L, output.data)
        assertEquals(938, output.id)
        assertEquals(TestBinaryType.LONG, output.type)
    }

    @Test
    fun testLongArray() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeLongArray(64, longArrayOf(874L, 223L, 1234L))
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)
        assertArrayEquals(longArrayOf(874L, 223L, 1234L), output.data as LongArray)
        assertEquals(64, output.id)
        assertEquals(TestBinaryType.LONG_ARRAY, output.type)
    }

    @Test
    fun testDouble() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeDouble(701 , 5.7)
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)
        assertEquals(5.7, output.data)
        assertEquals(701, output.id)
        assertEquals(TestBinaryType.DOUBLE, output.type)
    }

    @Test
    fun testDoubleArray() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeDoubleArray(72, doubleArrayOf(5.1, 8.1, 9.1))
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)

        val doubleArray = output.data as DoubleArray
        assertEquals(5.1, doubleArray[0], DOUBLE_DELTA)
        assertEquals(8.1, doubleArray[1], DOUBLE_DELTA)
        assertEquals(9.1, doubleArray[2], DOUBLE_DELTA)

        assertEquals(72, output.id)
        assertEquals(TestBinaryType.DOUBLE_ARRAY, output.type)
    }

    @Test
    fun testBoolean() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeBoolean(99, true)
        }
        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)
        assertEquals(true, output.data)
        assertEquals(99, output.id)
        assertEquals(TestBinaryType.BOOLEAN, output.type)
    }

    @Test
    fun testBooleanArray() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeBooleanArray(123, booleanArrayOf(true, false))
        }
        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)
        assertArrayEquals(booleanArrayOf(true, false), output.data as BooleanArray)
        assertEquals(123, output.id)
        assertEquals(TestBinaryType.BOOLEAN_ARRAY, output.type)
    }

    @Test
    fun testString() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeString(22, "Hello=world")
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)

        streamIn.read(output)
        assertEquals("Hello=world", output.data)
        assertEquals(22, output.id)
        assertEquals(TestBinaryType.STRING, output.type)
    }

    @Test
    fun testStringMatrix() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeStringMatrix(56, StringMatrix(listOf("name", "customer", "end")))
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)
        streamIn.read(output)

        val matrix = output.data as StringMatrix


        assertEquals("name", matrix[0])
        assertEquals("customer", matrix[1])
        assertEquals("end", matrix[2])
        assertEquals(56, output.id)
        assertEquals(TestBinaryType.STRING_MATRIX, output.type)
    }

    @Test
    fun testObject() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            val binOb = BinaryObject(83,
                arrayOf(
                    BinaryRecord(4, 8.2),
                    BinaryRecord(67, "tennis"),
                    BinaryRecord(55, true)
                )
            )

            out.writeObject(82367, binOb)
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)
        streamIn.read(output)

        val theObject = output.data as BinaryObject

        val isDebug = theObject.findId(55)?.data as Boolean
        assertEquals(isDebug, true)

        assertEquals(8.2, theObject[0].data)
        assertEquals("tennis", theObject[1].data)
        assertEquals(true, theObject[2].data)
        assertEquals(TestBinaryType.OBJECT, output.type)
    }

    @Test
    fun testObjectClassAndId() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            val binOb = BinaryObject(569,
                arrayOf(
                    BinaryRecord(2, "Hello Object"),
                    BinaryRecord(9, false)
                )
            )

            out.writeObject(777, binOb)
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)
        streamIn.read(output)
        assertEquals(output.id, 777)
        assertTrue(output.data is BinaryObject)

        val binObject = output.data as BinaryObject

        assertEquals(569, binObject.classId)
    }

    @Test
    fun testGuid() {
        val customerGuid = ClientGuid.create()
        //assertEquals("", customerGuid.first.guid)
    }
}