package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

class EngineDeserialized(
    val currentRuntimeId: Long,
    val coreScripts: List<KClass<*>>,
    val graph: NodeGraph
) {
    fun toEngineData(): EngineData {
        return EngineData(
            currentRuntimeId = currentRuntimeId,
            coreScripts = coreScripts.toMutableList(),
            graph = graph
        )
    }
}