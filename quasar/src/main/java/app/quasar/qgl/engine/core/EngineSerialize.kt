package app.quasar.qgl.engine.core

import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.QGLBinary
import app.quasar.qgl.serialize.QGLMapper
import java.io.File

class EngineSerialize(
    filename: String,
    engine: QuasarEngineActual
) {
    init {
        QGLBinary.createFileOut(File("${filename}.qgl")) { out ->
            val graph = engine.nodeGraph

            graph.forEach { node ->
                val mapper = node.getMapper() as QGLMapper<Any>
                val binaryRecord = mapper.toBinary(node.record.data!!)

                out.writeObject(8, BinaryObject(3, binaryRecord))
            }
        }
    }
}