package app.quasar.qgl.serialize

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream

class QGLAnySerializeTest {

    @Test
    fun notQglEntity() {
        var didThrow = false
        try {
            KClassMap(listOf(NotSerial::class))
        }catch (e: Exception) {
            didThrow = true
        }
        Assert.assertEquals(true, didThrow)
    }

    @Test
    fun testSerializeVector() {
        val vector = MyVector()
        vector.age = 18

        val stream = StringDataWriter()
        val binaryOut = QGLBinary().Out(stream)
        val coffee = CoffeeBin()

        val out = coffee.Out(binaryOut)
        out.write(listOf(vector))
        Assert.assertEquals("int:1;byte:1;int:1;int:3;byte:15;int:9;bytes:[-110,-122,-96,-119,-102,-100,-117,-112,-115,];int:2;byte:17;my_vector;int:2;int:1;byte:1;int:18;int:0;byte:15;int:0;bytes:[];", stream.toString())
    }

    @Test
    fun testSerializeAndDeserialize() {
        val vector = MyVector()
        vector.age = 22

        val stream = ByteArrayOutputStream()

        val binaryOut = QGLBinary().Out(BinaryDataWriter(stream))
        val coffee = CoffeeBin()

        val out = coffee.Out(binaryOut)
        out.write(listOf(vector))
        //Read back the data

        val qglIn = QGLBinary().In(DataInputStream(ByteArrayInputStream(stream.toByteArray())))

        val coffeeIn = coffee.In(KClassMap.of(MyVector::class), qglIn)
        val readBackVector = coffeeIn.read().first() as MyVector

        Assert.assertEquals(22, readBackVector.age)
        Assert.assertNotEquals(vector.notSerial, readBackVector.notSerial)
    }
}

@QGLEntity("my_vector")
class MyVector {

    @BinProp(typeId = 0)        var name = ""
    @BinProp(typeId = 1)        var age: Int = 99

    var notSerial: Long = System.currentTimeMillis()
}

class NotSerial