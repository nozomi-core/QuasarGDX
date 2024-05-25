package app.quasar.qgl.serialize

import com.badlogic.gdx.math.Vector3
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream

class CoffeeBinTest {

    @Test
    fun testSerial3rdLibraryVector() {
        val vector = Vector3()
        vector.x = 455f

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
    }
}