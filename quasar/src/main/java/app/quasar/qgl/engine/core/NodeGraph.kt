package app.quasar.qgl.engine.core

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class NodeGraph {

    private val nodeList = mutableListOf<GameNode<*>>()
    private val afterSimulationActions: Queue<EngineAction> = LinkedList()

    internal fun simulate(simContext: SimContext) {
        nodeList.forEach {
            it.simulate(simContext)
        }
        //Perform any actions after simulation
        while(afterSimulationActions.isNotEmpty()) {
            afterSimulationActions.remove().also { it() }
        }
    }

    internal fun draw(context: DrawContext) {
        nodeList.forEach {
            it.draw(context)
        }
    }

    internal fun <T : GameNode<*>> createNode(script: KClass<T>, factories: List<NodeFactoryCallback>) {
        scheduleAfterSimulationEvent {
            val newNode = script.createInstance()
            newNode.create(factories)
            nodeList.add(newNode)
        }
    }

    internal fun destroyNode(node: GameNode<*>) {
        scheduleAfterSimulationEvent {
            nodeList.remove(node)
            node.destroy()
        }
    }

    internal fun findNodeByTag(tag: String): NodeReference<ReadableGameNode>? {
        return nodeList.find { it.record.tag == tag }?.reference
    }

    internal fun queryAll(): List<NodeReference<ReadableGameNode>> = nodeList.mapNotNull { it.reference }

    private fun scheduleAfterSimulationEvent(action: EngineAction) {
        afterSimulationActions.add(action)
    }
}
typealias EngineAction = () -> Unit