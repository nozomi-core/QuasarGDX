package app.quasar.qgl.engine.core

import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.BinaryOutput
import app.quasar.qgl.serialize.QGLBinary
import java.io.File

class EngineDeserialize(filename: String) {

    init {
        val input = QGLBinary.createFileIn(File("${filename}.qgl"))

        val output = BinaryOutput()
        while(input.read(output)) {
            val dat = output.data as BinaryObject

            for(i in 0 until dat.size) {
                println(dat[i].data)
            }
        }
    }
}