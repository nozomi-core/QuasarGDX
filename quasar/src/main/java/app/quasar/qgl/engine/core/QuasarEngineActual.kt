package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

/**
 * The main structure of [QuasarEngine] implementation consists of engine submodules
 * Primary responsibility of [QuasarEngineActual] is to delegate API calls to the relevant submodules and receive events from them
 * and delegate any calls to the required module.
 */
class QuasarEngineActual: QuasarEngine {

    private val nodeGraph = NodeGraph()

    override fun simulate() {
        nodeGraph.simulate()
    }

    override fun <T : GameNode<*>> createNode(script: KClass<T>, factory: (NodeFactory) -> Unit) {
        nodeGraph.createNode(script, factory)
    }

    override fun findByTag(tag: String): ReadableGameNode? {
        return nodeGraph.findByTag(tag)
    }
}