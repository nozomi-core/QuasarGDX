package app.quasar.qgl.engine.core1

import kotlin.reflect.KClass

class EngineData1(
    var currentRuntimeId: Long,
    var coreScripts: MutableList<KClass<*>>,
    var graph: NodeGraph1,
    val clock: EngineClock1
) {
    companion object {
        fun createDefault(): EngineData1 {
            return EngineData1(
                currentRuntimeId = 1L,
                clock = EngineClock1(),
                coreScripts =  mutableListOf(),
                graph = NodeGraph1()
            )
        }
    }
}