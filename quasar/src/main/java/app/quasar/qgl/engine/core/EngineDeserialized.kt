package app.quasar.qgl.engine.core

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