package app.quasar.qgl.serialize

import org.junit.Assert
import org.junit.Test

class QGLBinaryFrameTest {

    @Test
    fun testFrameStrings() {
        val inMemory = QGLBinary.createMemoryOut { out ->
            out.writeString(1, "int-1,")
            out.writeString(2, "int-2")
            out.writeFrameStart(11)
            out.writeString(1, "next-1,")
            out.writeString(2, "next-2")
        }

        val output = BinaryOutput()
        val streamIn = QGLBinary.createMemoryIn(inMemory)
        val builder = StringBuilder()

        do {
            streamIn.read(output)
            if(output.isFrame()) {
                builder.append("|")
            } else {
                val strData = output.data as String
                builder.append(strData)
            }

        } while (output.hasData())
        Assert.assertEquals("int-1,int-2|next-1,next-2next-2", builder.toString())
    }
}