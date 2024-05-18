package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

/**
 * The main structure of [QuasarEngine] implementation consists of engine submodules
 * Primary responsibility of [QuasarEngineActual] is to delegate API calls to the relevant submodules and receive events from them
 * and delegate any calls to the required module.
 */
class QuasarEngineActual(factory: QuasarEngineFactory.() -> Unit = {}): QuasarEngine {
    var isRunning = true
        private set

    internal val nodeGraph: NodeGraph
    private val engineClock: EngineClock
    private val accounting: EngineAccounting

    private val simContext: SimContext
    private val drawContext: DrawContext

    init {
        val config = QuasarEngineFactory(factory)

        nodeGraph = createOrLoadGraph(config.nodeGraph)
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
        accounting = config.accounting ?: EngineAccounting(runtimeGameId = 10000)
    }

    private val engineNodeFactory: NodeFactoryCallback = { factory ->
        factory.nodeId = accounting.nextId()
        factory.engine = this
    }

    override fun <T : GameNode<*>> createNode(script: KClass<T>, factory: (NodeFactory) -> Unit) {
        nodeGraph.createNode(this, script, listOf(engineNodeFactory, factory))
    }

    override fun destroyNode(node: GameNode<*>) {
        nodeGraph.destroyNode(node)
    }

    override fun saveToFile(filename: String) {
        EngineSerialize(filename, this)
    }

    override fun shutdown() {
        isRunning = false
    }

    override fun pause() {
        isRunning = false
    }

    override fun resume() {
        isRunning = true
    }

    override fun queryNodeByTag(tag: String): NodeReference<ReadableGameNode>? {
        return nodeGraph.findNodeByTag(tag)
    }

    override fun queryAll(): List<NodeReference<ReadableGameNode>> {
        return nodeGraph.queryAll()
    }

    internal fun simulate(deltaTime: Float) {
        if(isRunning) {
            engineClock.deltaTime = deltaTime
            nodeGraph.simulate(simContext)
        }
    }

    internal fun draw() {
        nodeGraph.draw(drawContext)
    }

    private fun createOrLoadGraph(nodeGraph: NodeGraph?): NodeGraph {
        val graph = nodeGraph ?: NodeGraph(mutableListOf())
        graph.forEach {
            it.attachEngine(this)
        }
        return graph
    }
}