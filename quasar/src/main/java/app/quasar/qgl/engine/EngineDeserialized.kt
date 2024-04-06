package app.quasar.qgl.engine

import app.quasar.qgl.entity.NodeGraph
import app.quasar.qgl.serialize.QGLBinary
import app.quasar.qgl.serialize.QGLDefinitions
import kotlin.reflect.KClass

class EngineDeserialized(
    val currentRuntimeId: Long,
    val rootScripts: List<KClass<*>>,
    val graph: NodeGraph
) {

    private fun writeEngine(
        definitions: QGLDefinitions,
        out: QGLBinary.Out
    ) {






    }


}