package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

/**
 * The main structure of [QuasarEngine] implementation consists of engine submodules
 * Primary responsibility of [QuasarEngineActual] is to delegate API calls to the relevant submodules and receive events from them
 * and delegate any calls to the required module.
 */
class QuasarEngineActual(factory: QuasarEngineFactory.() -> Unit): QuasarEngine {
    private val nodeGraph: NodeGraph
    private val engineClock: EngineClock

    private val simContext: SimContext
    private val drawContext: DrawContext

    init {
        val config = QuasarEngineFactory(factory)

        nodeGraph = NodeGraph()
        engineClock = EngineClock()
        simContext = SimContext()
        drawContext = DrawContext(draw = config.requireDrawableApi())
    }

    override fun <T : GameNode<*>> createNode(script: KClass<T>, factory: (NodeFactory) -> Unit) {
        nodeGraph.createNode(script, factory)
    }

    override fun findByTag(tag: String): ReadableGameNode? {
        return nodeGraph.findByTag(tag)
    }

    internal fun simulate(deltaTime: Float) {
        engineClock.update(deltaTime)
        nodeGraph.simulate(simContext)
    }

    internal fun draw() {
        nodeGraph.draw(drawContext)
    }
}