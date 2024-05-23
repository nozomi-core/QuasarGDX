package app.quasar.qgl.serialize

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

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
        out.write(vector)
        Assert.assertEquals("", stream.toString())
    }

    @Test
    fun testSerializeAndDeserialize() {
        val vector = MyVector()
        vector.age = 22

        val stream = ByteArrayOutputStream()

        val binaryOut = QGLBinary().Out(BinaryDataWriter(stream))
        val coffee = CoffeeBin()

        val out = coffee.Out(binaryOut)
        out.write(vector)
        //Read back the data

        val qglIn = QGLBinary().In(DataInputStream(ByteArrayInputStream(stream.toByteArray())))

        val coffeeIn = coffee.In(KClassMap.of(MyVector::class), qglIn)
        val readBackVector = coffeeIn.read() as MyVector

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