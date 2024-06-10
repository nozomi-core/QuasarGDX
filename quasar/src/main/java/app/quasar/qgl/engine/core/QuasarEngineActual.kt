package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.serialize.ClassFactory
import app.quasar.qgl.engine.serialize.EngineSerialize
import app.quasar.qgl.serialize.BinaryDataWriter
import app.quasar.qgl.serialize.QGLBinary
import java.io.File
import java.io.FileOutputStream
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

    internal val accounting: EngineAccounting

    private val worldGraph: WorldGraph
    private val engineClock: EngineClock
    private val simContext: SimContext
    private val drawContext: DrawContext

    private val scriptFactory: ClassFactory
    private var dimension: EngineDimension = EngineDimension.default()

    private val overlayGraph: OverlayGraph
    private val shapes: ShapeApi

    //TODO: consider calling this dimension graph. Have NodeGraph and DimensionGraph have same graph/search operators
    private val drawableZGraph: DrawableZGraph

    override val current: EngineDimension
        get() = dimension

    init {
        val config = QuasarEngineFactory(factory)

        val project = config.requireProject()

        val draw = config.requireDrawableApi()

        nodeGraph = createOrLoadGraph(config.nodeGraph)
        engineClock = EngineClock()
        simContext = SimContext(
            engine = this,
            clock = engineClock,
            project = project,
            camera = config.requireCamera(),
            render = draw
        )
        drawContext = DrawContext(
            draw = draw,
            project = project,
        )
        accounting = config.accounting ?: EngineAccounting(runtimeGameId = 10000)
        scriptFactory = config.classes!!
        worldGraph = WorldGraph(nodeGraph)
        overlayGraph = OverlayGraph(nodeGraph)
        shapes = config.requireShapes()
        drawableZGraph = DrawableZGraph(nodeGraph)
    }

    private val engineNodeFactory: NodeFactoryCallback = { factory ->
        factory.nodeId = accounting.nextId()
        factory.engine = this
    }

    override fun setDimension(dimension: EngineDimension) {
        this.dimension = dimension
        drawableZGraph.setDimension(dimension, nodeGraph)
    }

    override fun <T : GameNode<*>> createNode(dimension: EngineDimension, script: KClass<T>, factory: (NodeFactory) -> Unit) {
        val dimenNodeFactory: NodeFactoryCallback = { local ->
            local.dimension = dimension
        }

        nodeGraph.createNode(this, script, listOf(engineNodeFactory, factory, dimenNodeFactory))
    }

    override fun <T : GameNode<D>, D> replace(node: GameNode<D>, replaceScript: KClass<T>) {
        nodeGraph.replace(this, node, replaceScript)
    }

    override fun notifyDimensionChanged(node: GameNode<*>) {
        drawableZGraph.notifyDimensionChanged(node)
    }

    override fun destroyNode(node: GameNode<*>) {
        nodeGraph.destroyNode(node)
    }

    override fun saveToFile(filename: String) {
        EngineSerialize(this) {
            QGLBinary().Out(BinaryDataWriter(FileOutputStream(File(filename))))
        }
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

    override fun generateId(): Long {
        return accounting.nextId()
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

    internal fun draw(overlayScreen: WindowScreen) {
        drawContext.update(overlayScreen)
        drawableZGraph.draw(drawContext)
    }

    internal fun drawOverlay() {
        overlayGraph.draw(current, drawContext)
    }

    private fun createOrLoadGraph(nodeGraph: NodeGraph?): NodeGraph {
        val graph = nodeGraph ?: NodeGraph(mutableListOf())
        graph.forEach {
            it.attachEngine(this)
        }
        return graph
    }
}