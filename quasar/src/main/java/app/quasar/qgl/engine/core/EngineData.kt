package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

class EngineData(
    var currentRuntimeId: Long,
    var coreScripts: MutableList<KClass<*>>,
    var graph: NodeGraph,
    val clock: EngineClock
) {
    companion object {
        fun createDefault(): EngineData {
            return EngineData(
                currentRuntimeId = 1L,
                clock = EngineClock(),
                coreScripts =  mutableListOf(),
                graph = NodeGraph()
            )
        }
    }
}