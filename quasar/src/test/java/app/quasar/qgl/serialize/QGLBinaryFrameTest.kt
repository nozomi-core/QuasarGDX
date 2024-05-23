package app.quasar.qgl.serialize

import org.junit.Assert
import org.junit.Test

class QGLBinaryFrameTest {

    @Test
    fun testFrameStrings() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeFrame(11) {
                out.writeString(1, "int-1,")
                out.writeString(2, "int-2")
            }
            out.writeFrame(22) {
                out.writeString(1, "next-1,")
                out.writeString(2, "next-2")
            }
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)
        val builder = StringBuilder()

        do {
            streamIn.read(output)
            if(output.type == 20 && output.id == 11) {
                builder.append("|")
            } else {
                val strData = output.data as String
                builder.append(strData)
            }

        } while (output.hasData())
        Assert.assertEquals("int-1,int-2|next-1,next-2next-2", builder.toString())
    }

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