package app.quasar.qgl.serialize

import com.badlogic.gdx.math.Vector3
import org.junit.Assert
import org.junit.Test
class CoffeeBinTest {

    @Test
    fun testSerial3rdLibraryVector() {
        val propVector = 2

        val classIdVector = 3

        val vector = Vector3(800f,801f,802f)

        val binaryVector = BinaryObject(classIdVector, arrayOf(
            BinaryRecord(4, vector.x),
            BinaryRecord(5, vector.y),
            BinaryRecord(6, vector.z)
        ))

        val records = arrayOf(
            BinaryRecord(propVector, binaryVector)
        )

        val memIn = QGLBinary.createMemoryOut { out ->
            out.writeObject(77, BinaryObject(111, records))
        }

        val input = QGLBinary.createMemoryIn(memIn)
        val output = BinaryOutput()

        input.read(output)
        val rootObject = output.data as BinaryObject

        val vectorRecord = rootObject.findId(propVector)!!

        val vectorBinaryObject = vectorRecord.data as BinaryObject

        val readBack = Vector3(
            vectorBinaryObject.value(4),
            vectorBinaryObject.value(5),
            vectorBinaryObject.value(6)
        )
        Assert.assertEquals(readBack.x, 800f)
        Assert.assertEquals(readBack.y, 801f)
        Assert.assertEquals(readBack.z, 802f)
    }
}