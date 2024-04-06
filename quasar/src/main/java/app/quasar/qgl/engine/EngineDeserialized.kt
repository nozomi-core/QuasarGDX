package app.quasar.qgl.engine

import app.quasar.qgl.entity.NodeGraph
import kotlin.reflect.KClass

class EngineDeserialized(
    val currentRuntimeId: Long,
    val graph: NodeGraph,
    val rootScripts: List<KClass<*>>
)