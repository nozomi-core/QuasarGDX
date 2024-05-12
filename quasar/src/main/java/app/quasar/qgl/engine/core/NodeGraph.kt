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
        //Perform any actions post simulation
        while(afterSimulationActions.isNotEmpty()) {
            val action = afterSimulationActions.remove()
            action()
        }
    }

    internal fun draw(context: DrawContext) {
        nodeList.forEach {
            it.draw(context)
        }
    }

    internal fun <T : GameNode<*>> createNode(engine: QuasarEngine, script: KClass<T>, factory: (NodeFactory) -> Unit) {
        scheduleAfterSimulationEvent {
            val newNode = script.createInstance()
            newNode.create(engine, NodeFactory(factory))
            nodeList.add(newNode)
        }
    }

    internal fun destroyNode(node: GameNode<*>) {
        scheduleAfterSimulationEvent {
            nodeList.remove(node)
            node.destroy()
        }
    }

    internal fun findByTag(tag: String): ReadableGameNode? {
        return nodeList.find { it.tag == tag }
    }

    private fun scheduleAfterSimulationEvent(action: EngineAction) {
        afterSimulationActions.add(action)
    }
}
typealias EngineAction = () -> Unit