package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

class EngineData(
    var currentRuntimeId: Long,
    var coreScripts: MutableList<KClass<*>>,
    var graph: NodeGraph
) {
    companion object {
        fun createDefault(): EngineData {
            return EngineData(currentRuntimeId = 1L, mutableListOf(), NodeGraph())
        }
    }
}