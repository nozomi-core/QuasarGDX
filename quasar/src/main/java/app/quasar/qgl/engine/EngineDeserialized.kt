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
    fun toEngineData(): EngineData {
        return EngineData(
            currentRuntimeId = currentRuntimeId,
            rootScripts = rootScripts.toMutableList(),
            graph = graph
        )
    }
}