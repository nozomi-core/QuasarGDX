package app.quasar.qgl.engine.core

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class NodeGraph(
    private val nodeList: MutableList<GameNode<*>>
) {
    private val afterSimulationActions: Queue<EngineAction> = LinkedList()
    private val listeners = mutableListOf<GraphListener>()

    internal val size: Int get() = nodeList.size

    internal fun forEach(callback: (GameNode<*>) -> Unit) {
        nodeList.forEach(callback)
    }

    internal fun simulate(simContext: SimContext) {
        nodeList.forEach {
            it.simulate(simContext)
        }
        afterSimulation()
    }

    private fun afterSimulation() {
        while(afterSimulationActions.isNotEmpty()) {
            afterSimulationActions.remove().also { it() }
        }
    }

    internal fun <T : GameNode<*>> createNode(engine: QuasarEngine, script: KClass<T>, factories: List<NodeFactoryCallback>) {
        scheduleAfterSimulationEvent {
            val newNode = script.createInstance()
            newNode.create(factories)
            newNode.attachEngine(engine)
            nodeList.add(newNode)
            notifyAdded(newNode)
        }
    }

    internal fun <T : GameNode<D>, D> replace(engine: QuasarEngine, node: GameNode<D>, replaceScript: KClass<T>) {
        scheduleAfterSimulationEvent {
            val replaceNode = replaceScript.createInstance()
            replaceNode.record = NodeRecord(
                nodeId = engine.generateId(),
                tag = "",
                data = node.record.data,
                dimension = node.selfDimension
            )
            replaceNode.attachEngine(engine)

            nodeList.remove(node)
            nodeList.add(replaceNode)

            notifyRemoved(node)
            notifyAdded(replaceNode)
        }
    }

    internal fun destroyNode(node: GameNode<*>) {
        scheduleAfterSimulationEvent {
            nodeList.remove(node)
            notifyRemoved(node)
            node.doDestroy()
        }
    }

    internal fun findNodeByTag(tag: String): NodeReference<ReadableGameNode>? {
        return nodeList.find { it.record.tag == tag }?.reference
    }

    internal fun queryAll(): List<NodeReference<ReadableGameNode>> = nodeList.mapNotNull { it.reference }
    internal fun addListener(graphListener: GraphListener) {
        nodeList.forEach { graphListener.onAdded(it) }
        listeners.add(graphListener)
    }

    internal fun getNodesWithDimension(dimen: EngineDimension): List<GameNode<*>> {
        return nodeList.filter { it.selfDimension.id == dimen.id }
    }

    private fun notifyAdded(node: GameNode<*>) = listeners.forEach { it.onAdded(node) }
    private fun notifyRemoved(node: GameNode<*>) = listeners.forEach { it.onRemoved(node) }

    private fun scheduleAfterSimulationEvent(action: EngineAction) {
        afterSimulationActions.add(action)
    }
}

typealias EngineAction = () -> Unit

interface GraphListener {
    fun onAdded(node: GameNode<*>)
    fun onRemoved(node: GameNode<*>)
}