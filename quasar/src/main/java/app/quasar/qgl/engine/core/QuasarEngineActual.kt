package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

/**
 * The main structure of [QuasarEngine] implementation consists of engine submodules
 * Primary responsibility of [QuasarEngineActual] is to delegate API calls to the relevant submodules and receive events from them
 * and delegate any calls to the required module.
 */
class QuasarEngineActual(factory: QuasarEngineFactory.() -> Unit = {}): QuasarEngine {
    private val nodeGraph: NodeGraph
    private val engineClock: EngineClock

    private val simContext: SimContext
    private val drawContext: DrawContext

    init {
        val config = QuasarEngineFactory(factory)

        nodeGraph = NodeGraph()
        engineClock = EngineClock()
        simContext = SimContext(
            engine = this,
            clock = engineClock,
            project = config.requireProject()
        )
        drawContext = DrawContext(
            draw = config.requireDrawableApi(),
            camera = config.requireCamera()
        )
    }

    override fun <T : GameNode<*>> createNode(script: KClass<T>, factory: (NodeFactory) -> Unit) {
        nodeGraph.createNode(this, script, factory)
    }

    override fun destroyNode(node: GameNode<*>) {
        nodeGraph.destroyNode(node)
    }

    override fun findNodeByTag(tag: String): NodeReference<ReadableGameNode>? {
        return nodeGraph.findNodeByTag(tag)
    }

    internal fun simulate(deltaTime: Float) {
        engineClock.deltaTime = deltaTime
        nodeGraph.simulate(simContext)
    }

    internal fun draw() {
        nodeGraph.draw(drawContext)
    }
}