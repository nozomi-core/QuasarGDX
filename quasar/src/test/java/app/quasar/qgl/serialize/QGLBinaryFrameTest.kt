package app.quasar.qgl.serialize

import org.junit.Assert
import org.junit.Test

class QGLBinaryFrameTest {

    @Test
    fun `test string type frame following integer frame`() {
        //Test binary format [Frame=1][1][2][3]][Frame=2]["one"]["two"]["three"]
        val inMem = QGLBinary.createMemoryOut { out ->
            out.writeFrame(1) {
                out.writeInt(0,1)
                out.writeInt(0, 2)
                out.writeInt(0, 3)
            }

            out.writeFrame(2) {
                out.writeString(0, "one")
                out.writeString(0, "two")
            }
        }
        //read back
        val out = QGLBinary.createMemoryIn(inMem)
        val numbers = mutableListOf<Int>()
        out.readFrame { output ->
            numbers.add(output.data as Int)
        }

        val strNums = mutableListOf<String>()
        out.readFrame { output ->
            strNums.add(output.data as String)
        }
        Assert.assertEquals(2, strNums.size)
    }
}