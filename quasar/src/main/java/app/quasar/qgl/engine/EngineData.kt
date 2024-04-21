package app.quasar.qgl.engine

import app.quasar.qgl.entity.NodeGraph
import kotlin.reflect.KClass

class EngineData(
    var currentRuntimeId: Long,
    var rootScripts: MutableList<KClass<*>>,
    var graph: NodeGraph
) {
    companion object {
        fun createDefault(): EngineData {
            return EngineData(currentRuntimeId = 1L, mutableListOf(), NodeGraph())
        }
    }
}