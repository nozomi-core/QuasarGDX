package app.quasar.qgl.engine.core1

import kotlin.reflect.KClass

class EngineDeserialized1(
    val currentRuntimeId: Long,
    val coreScripts: List<KClass<*>>,
    val graph: NodeGraph1
) {
    fun toEngineData(): EngineData1 {
        return EngineData1(
            currentRuntimeId = currentRuntimeId,
            coreScripts = coreScripts.toMutableList(),
            graph = graph,
            clock = EngineClock1()
        )
    }
}