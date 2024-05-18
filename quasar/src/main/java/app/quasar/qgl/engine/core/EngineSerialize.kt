package app.quasar.qgl.engine.core

import app.quasar.qgl.serialize.QGLBinary
import java.io.File

class EngineSerialize(
    filename: String,
    engine: QuasarEngineActual
) {
    init {
        val graph = engine.nodeGraph

        QGLBinary.createFileOut(File("saves/${filename}")) {
            it.writeInt(22, graph.queryAll().size)
        }
    }
}